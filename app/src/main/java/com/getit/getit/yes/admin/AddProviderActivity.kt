package com.getit.getit.yes.admin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.getit.getit.yes.data.Categories
import com.getit.getit.yes.data.Category
import com.getit.getit.yes.data.Provider
import com.getit.getit.yes.data.ProviderRepository
import com.getit.getit.yes.data.ServiceType
import com.getit.getit.yes.databinding.ActivityAddProviderBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/** Open to any signed-in user; firestore.rules validate fields and stamp ownership via createdBy. */
class AddProviderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProviderBinding
    private var selectedCategory: Category? = null
    private var selectedType: ServiceType? = null

    private val locationPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        if (granted.values.any { it }) fillCurrentLocation()
        else Toast.makeText(this, R.string.location_permission_needed, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityAddProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.scroll) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime()
            )
            view.updatePadding(left = bars.left, right = bars.right, bottom = bars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setUpDropdowns()
        if (savedInstanceState == null) applyPrefill()
        binding.useLocation.setOnClickListener { requestCurrentLocation() }
        binding.save.setOnClickListener { save() }
    }

    private fun applyPrefill() {
        Categories.category(intent.getStringExtra(EXTRA_CATEGORY).orEmpty())?.let { category ->
            selectCategory(category)
            binding.category.setText(category.label, false)
            category.types.firstOrNull { it.id == intent.getStringExtra(EXTRA_TYPE) }?.let { type ->
                selectedType = type
                binding.type.setText(type.label, false)
            }
        }
        if (intent.hasExtra(EXTRA_LAT) && intent.hasExtra(EXTRA_LNG)) {
            setCoordinates(intent.getDoubleExtra(EXTRA_LAT, 0.0), intent.getDoubleExtra(EXTRA_LNG, 0.0))
        }
    }

    private fun setCoordinates(latitude: Double, longitude: Double) {
        binding.latitude.setText("%.6f".format(java.util.Locale.US, latitude))
        binding.longitude.setText("%.6f".format(java.util.Locale.US, longitude))
    }

    private fun selectCategory(category: Category) {
        selectedCategory = category
        selectedType = null
        binding.type.setText("", false)
        binding.type.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, category.types.map { it.label })
        )
        binding.categoryLayout.error = null
    }

    private fun setUpDropdowns() {
        binding.category.setSimpleItems(Categories.all.map { it.label }.toTypedArray())
        binding.category.setOnItemClickListener { _, _, position, _ -> selectCategory(Categories.all[position]) }
        binding.type.setOnItemClickListener { _, _, position, _ ->
            selectedType = selectedCategory?.types?.get(position)
            binding.typeLayout.error = null
        }
    }

    private fun requestCurrentLocation() {
        val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        if (granted) {
            fillCurrentLocation()
        } else {
            locationPermission.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun fillCurrentLocation() {
        setLoading(true)
        lifecycleScope.launch {
            val location = runCatching {
                LocationServices.getFusedLocationProviderClient(this@AddProviderActivity)
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .await()
            }.getOrNull()
            setLoading(false)
            if (location == null) {
                Toast.makeText(this@AddProviderActivity, R.string.location_unavailable, Toast.LENGTH_SHORT).show()
                return@launch
            }
            setCoordinates(location.latitude, location.longitude)
        }
    }

    private fun save() {
        val name = binding.name.text?.toString()?.trim().orEmpty()
        val phone = binding.phone.text?.toString()?.trim().orEmpty()
        val imageUrl = binding.imageUrl.text?.toString()?.trim().orEmpty()
        val latitude = binding.latitude.text?.toString()?.toDoubleOrNull()
        val longitude = binding.longitude.text?.toString()?.toDoubleOrNull()

        var valid = true
        fun check(layout: TextInputLayout, ok: Boolean, error: Int = R.string.error_required) {
            layout.error = if (ok) null else getString(error)
            if (!ok) valid = false
        }
        check(binding.nameLayout, name.isNotEmpty())
        check(binding.phoneLayout, phone.isNotEmpty())
        check(binding.categoryLayout, selectedCategory != null)
        check(binding.typeLayout, selectedType != null)
        check(binding.imageUrlLayout, imageUrl.isEmpty() || imageUrl.startsWith("https://"), R.string.error_invalid_url)
        val coordinatesOk = latitude != null && longitude != null &&
            latitude in -90.0..90.0 && longitude in -180.0..180.0
        check(binding.latitudeLayout, coordinatesOk, R.string.error_invalid_coordinates)
        check(binding.longitudeLayout, coordinatesOk, R.string.error_invalid_coordinates)
        if (!valid) return

        val provider = Provider(
            id = "",
            name = name,
            phone = phone,
            category = selectedCategory!!.id,
            type = selectedType!!.id,
            latitude = latitude!!,
            longitude = longitude!!,
            imageUrl = imageUrl,
            address = binding.address.text?.toString()?.trim().orEmpty(),
            description = binding.description.text?.toString()?.trim().orEmpty(),
        )

        setLoading(true)
        lifecycleScope.launch {
            try {
                ProviderRepository.add(provider)
                Toast.makeText(this@AddProviderActivity, R.string.provider_saved, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
                Toast.makeText(
                    this@AddProviderActivity,
                    e.localizedMessage ?: getString(R.string.generic_error),
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.save.isEnabled = !loading
        binding.useLocation.isEnabled = !loading
    }

    companion object {
        private const val EXTRA_CATEGORY = "category"
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_LAT = "lat"
        private const val EXTRA_LNG = "lng"

        fun intent(context: Context, category: String? = null, type: String? = null, location: LatLng? = null): Intent =
            Intent(context, AddProviderActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, category)
                putExtra(EXTRA_TYPE, type)
                location?.let {
                    putExtra(EXTRA_LAT, it.latitude)
                    putExtra(EXTRA_LNG, it.longitude)
                }
            }
    }
}
