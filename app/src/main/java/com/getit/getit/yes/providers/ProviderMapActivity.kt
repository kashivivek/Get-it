package com.getit.getit.yes.providers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.getit.getit.yes.R
import com.getit.getit.yes.admin.AddProviderActivity
import com.getit.getit.yes.data.Categories
import com.getit.getit.yes.data.Provider
import com.getit.getit.yes.data.ProviderRepository
import com.getit.getit.yes.databinding.ActivityProviderMapBinding
import com.getit.getit.yes.databinding.SheetProviderBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapColorScheme
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class ProviderMapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProviderMapBinding
    private var map: GoogleMap? = null
    private var permissionResult: CompletableDeferred<Boolean>? = null

    private val category by lazy { intent.getStringExtra(EXTRA_CATEGORY).orEmpty() }
    private val type by lazy { intent.getStringExtra(EXTRA_TYPE).orEmpty() }

    private val locationPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted -> permissionResult?.complete(granted.values.any { it }) }

    private val addProvider = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) map?.let { refreshMarkers(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityProviderMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(left = bars.left, right = bars.right, bottom = bars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(EXTRA_LABEL)?.takeIf { it.isNotBlank() }
            ?: Categories.type(category, type)?.label
            ?: getString(R.string.app_name)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.addProvider.setOnClickListener {
            val center = map?.cameraPosition?.target
            addProvider.launch(AddProviderActivity.intent(this, category, type, center))
        }

        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { googleMap ->
            map = googleMap
            googleMap.mapColorScheme = if (isNightMode()) MapColorScheme.DARK else MapColorScheme.LIGHT
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.setOnMarkerClickListener { marker ->
                (marker.tag as? Provider)?.let(::showProviderSheet)
                true
            }
            load(googleMap)
        }
    }

    private fun load(googleMap: GoogleMap) {
        binding.progress.visibility = View.VISIBLE
        lifecycleScope.launch {
            val providersJob = async { runCatching { ProviderRepository.list(category, type) } }
            val userLocation = if (ensureLocationPermission()) {
                enableMyLocation()
                currentLocation()
            } else {
                null
            }
            val result = providersJob.await()
            binding.progress.visibility = View.GONE

            val providers = result.getOrElse {
                showMessage(getString(R.string.load_providers_failed))
                emptyList()
            }
            addMarkers(googleMap, providers)
            positionCamera(googleMap, userLocation, providers, showMessages = result.isSuccess)
        }
    }

    private fun refreshMarkers(googleMap: GoogleMap) {
        lifecycleScope.launch {
            runCatching { ProviderRepository.list(category, type) }.onSuccess { providers ->
                addMarkers(googleMap, providers)
                binding.emptyMessage.visibility = View.GONE
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap, providers: List<Provider>) {
        googleMap.clear()
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.u)
        providers.forEach { provider ->
            googleMap.addMarker(
                MarkerOptions().position(provider.latLng).title(provider.name).icon(icon)
            )?.tag = provider
        }
    }

    private fun positionCamera(googleMap: GoogleMap, user: LatLng?, providers: List<Provider>, showMessages: Boolean) {
        if (user != null) {
            val nearby = providers.filter { distanceKm(user, it.latLng) <= NEARBY_RADIUS_KM }
            if (nearby.isEmpty()) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, USER_ZOOM))
                if (showMessages) {
                    showMessage(
                        if (providers.isEmpty()) getString(R.string.no_providers)
                        else resources.getQuantityString(R.plurals.no_providers_nearby, providers.size, providers.size)
                    )
                }
            } else {
                fitBounds(googleMap, nearby.map { it.latLng } + user)
            }
            return
        }
        when {
            providers.size == 1 -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(providers[0].latLng, 15f))
            providers.isNotEmpty() -> fitBounds(googleMap, providers.map { it.latLng })
            showMessages -> showMessage(getString(R.string.no_providers))
        }
    }

    private fun fitBounds(googleMap: GoogleMap, points: List<LatLng>) {
        val bounds = LatLngBounds.builder().apply { points.forEach(::include) }.build()
        val padding = resources.getDimensionPixelSize(R.dimen.map_bounds_padding)
        binding.map.post {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            if (googleMap.cameraPosition.zoom > 16f) googleMap.moveCamera(CameraUpdateFactory.zoomTo(16f))
        }
    }

    private fun showProviderSheet(provider: Provider) {
        val dialog = BottomSheetDialog(this)
        val sheet = SheetProviderBinding.inflate(layoutInflater)
        sheet.name.text = provider.name
        sheet.subtitle.text = listOfNotNull(
            Categories.type(provider.category, provider.type)?.label,
            provider.phone.takeIf { it.isNotBlank() },
        ).joinToString(" · ")
        sheet.directions.setOnClickListener { dialog.dismiss(); ProviderActions.directions(this, provider) }
        sheet.call.setOnClickListener { dialog.dismiss(); ProviderActions.call(this, provider) }
        sheet.profile.setOnClickListener {
            dialog.dismiss()
            startActivity(ProviderDetailActivity.intent(this, provider))
        }
        dialog.setContentView(sheet.root)
        dialog.show()
    }

    private fun showMessage(message: String) {
        binding.emptyMessage.text = message
        binding.emptyMessage.visibility = View.VISIBLE
    }

    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private suspend fun ensureLocationPermission(): Boolean {
        if (hasLocationPermission()) return true
        val result = CompletableDeferred<Boolean>().also { permissionResult = it }
        locationPermission.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
        return result.await()
    }

    @SuppressLint("MissingPermission")
    private suspend fun currentLocation(): LatLng? {
        val client = LocationServices.getFusedLocationProviderClient(this)
        val location: Location? = runCatching { client.lastLocation.await() }.getOrNull()
            ?: withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
                runCatching { client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).await() }
                    .getOrNull()
            }
        return location?.let { LatLng(it.latitude, it.longitude) }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        }
    }

    private fun isNightMode() =
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    private fun distanceKm(a: LatLng, b: LatLng): Float {
        val result = FloatArray(1)
        Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, result)
        return result[0] / 1000f
    }

    private val Provider.latLng get() = LatLng(latitude, longitude)

    companion object {
        private const val EXTRA_CATEGORY = "category"
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_LABEL = "label"
        private const val NEARBY_RADIUS_KM = 25f
        private const val USER_ZOOM = 13f
        private const val LOCATION_TIMEOUT_MS = 5_000L

        fun intent(context: Context, category: String, type: String, label: String): Intent =
            Intent(context, ProviderMapActivity::class.java)
                .putExtra(EXTRA_CATEGORY, category)
                .putExtra(EXTRA_TYPE, type)
                .putExtra(EXTRA_LABEL, label)
    }
}
