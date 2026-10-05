// Uses Application Default Credentials: either `gcloud auth application-default login`
// with an owner account, or GOOGLE_APPLICATION_CREDENTIALS pointing at a service-account key.
import { initializeApp, applicationDefault } from 'firebase-admin/app';
import { getAuth } from 'firebase-admin/auth';
import { getFirestore, GeoPoint, FieldValue } from 'firebase-admin/firestore';

const PROJECT_ID = 'stellar-cumulus-108603';

initializeApp({ credential: applicationDefault(), projectId: PROJECT_ID });
const db = getFirestore();
const auth = getAuth();

// Migrated from the legacy get_it_db2.sql; legacy http:// image links were dead and are dropped.
const SEED_PROVIDERS = [
  { id: 'H1', name: 'Swami', phone: '54106352', category: 'household', type: 'carpenter', lat: 17.423261, lng: 78.49431 },
  { id: 'H2', name: 'Narsing', phone: '210512', category: 'household', type: 'plumbing', lat: 17.421769, lng: 78.493521 },
  { id: 'H3', name: 'Daya', phone: '21052', category: 'household', type: 'electrical', lat: 17.422203, lng: 78.492299 },
  { id: 'G1', name: 'Padmavathi Traders', phone: '+919145278954', category: 'groceries', type: 'hardware', lat: 17.422464, lng: 78.493921 },
  { id: 'G2', name: 'Raj Stores', phone: '+919963694916', category: 'groceries', type: 'kirana', lat: 17.425987, lng: 78.494756 },
  { id: 'G3', name: 'Sri Swathi Medical Stores', phone: '+918885657658', category: 'groceries', type: 'pharmacy', lat: 17.419325, lng: 78.491448 },
  { id: 'F1', name: 'Jai Mata Di', phone: '+914027634494', category: 'food', type: 'chaat', lat: 17.421785, lng: 78.49312 },
  { id: 'F2', name: 'Abbai Tiffins', phone: '', category: 'food', type: 'bakers', lat: 17.414916, lng: 78.490421 },
  { id: 'F3', name: 'Green Bawarchi', phone: '', category: 'food', type: 'restaurants', lat: 17.418162, lng: 78.491194 },
  { id: 'F4', name: 'Ashirwaad', phone: '', category: 'food', type: 'restaurants', lat: 17.423999, lng: 78.493349 },
];

async function seed() {
  const batch = db.batch();
  for (const p of SEED_PROVIDERS) {
    batch.set(
      db.collection('providers').doc(p.id),
      {
        name: p.name,
        phone: p.phone,
        category: p.category,
        type: p.type,
        location: new GeoPoint(p.lat, p.lng),
        imageUrl: '',
        address: '',
        description: '',
        createdAt: FieldValue.serverTimestamp(),
        createdBy: 'seed',
      },
      { merge: true },
    );
  }
  await batch.commit();
  console.log(`Seeded ${SEED_PROVIDERS.length} providers.`);
}

async function setAdmin(email, enabled) {
  if (!email) throw new Error('Usage: node index.js grant-admin|revoke-admin <email>');
  const user = await auth.getUserByEmail(email);
  const ref = db.collection('admins').doc(user.uid);
  if (enabled) {
    await ref.set({ email, grantedAt: FieldValue.serverTimestamp() });
    console.log(`${email} (${user.uid}) is now an admin.`);
  } else {
    await ref.delete();
    console.log(`${email} (${user.uid}) is no longer an admin.`);
  }
}

async function listAdmins() {
  const snap = await db.collection('admins').get();
  if (snap.empty) console.log('No admins.');
  snap.forEach((doc) => console.log(`${doc.id}\t${doc.get('email') ?? ''}`));
}

async function listReports() {
  const snap = await db.collection('reports').orderBy('createdAt', 'desc').get();
  if (snap.empty) console.log('No reports.');
  snap.forEach((doc) => {
    const r = doc.data();
    console.log(`${doc.id}\tprovider=${r.providerId}\tby=${r.reportedBy}\t${r.reason}`);
  });
}

async function deleteProvider(providerId) {
  if (!providerId) throw new Error('Usage: node index.js delete-provider <providerId>');
  await db.collection('providers').doc(providerId).delete();
  const reports = await db.collection('reports').where('providerId', '==', providerId).get();
  const batch = db.batch();
  reports.forEach((doc) => batch.delete(doc.ref));
  await batch.commit();
  console.log(`Deleted provider ${providerId} and ${reports.size} related report(s).`);
}

const [command, arg] = process.argv.slice(2);
const commands = {
  seed: () => seed(),
  'grant-admin': () => setAdmin(arg, true),
  'revoke-admin': () => setAdmin(arg, false),
  'list-admins': () => listAdmins(),
  'list-reports': () => listReports(),
  'delete-provider': () => deleteProvider(arg),
};

if (!commands[command]) {
  console.error(`Usage: node index.js <${Object.keys(commands).join('|')}> [email]`);
  process.exit(1);
}

commands[command]().catch((err) => {
  console.error(err.message ?? err);
  process.exit(1);
});
