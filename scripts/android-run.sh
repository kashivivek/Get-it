#!/usr/bin/env bash
# Pick a connected device or emulator, then build, install and launch the debug app.
set -euo pipefail
cd "$(dirname "$0")/.."

SDK="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
ADB="$SDK/platform-tools/adb"
EMULATOR="$SDK/emulator/emulator"
APP_ID="com.getit.getit.yes"
STUDIO_JDK="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
[[ -d "$STUDIO_JDK" ]] && export JAVA_HOME="$STUDIO_JDK"

"$ADB" start-server >/dev/null

labels=()
targets=()
while read -r serial state rest; do
  [[ "$state" == "device" ]] || continue
  model=$(sed -n 's/.*model:\([^ ]*\).*/\1/p' <<<"$rest")
  labels+=("${model:-$serial}  ($serial)")
  targets+=("device:$serial")
done < <("$ADB" devices -l | tail -n +2)

while read -r avd; do
  [[ -n "$avd" ]] || continue
  labels+=("Start emulator: $avd")
  targets+=("avd:$avd")
done < <("$EMULATOR" -list-avds 2>/dev/null || true)

if [[ ${#targets[@]} -eq 0 ]]; then
  echo "No devices or emulators found. Connect a phone with USB debugging enabled." >&2
  exit 1
fi

echo "Select a target:"
for i in "${!labels[@]}"; do printf "  %d) %s\n" "$((i + 1))" "${labels[$i]}"; done
read -rp "Choice [1]: " choice
choice=${choice:-1}
if ! [[ "$choice" =~ ^[0-9]+$ ]] || (( choice < 1 || choice > ${#targets[@]} )); then
  echo "Invalid choice" >&2
  exit 1
fi
target=${targets[$((choice - 1))]}

if [[ "$target" == avd:* ]]; then
  avd=${target#avd:}
  before=$("$ADB" devices | awk 'NR>1 && $1 ~ /^emulator-/ {print $1}')
  echo "Starting emulator $avd..."
  "$EMULATOR" -avd "$avd" >/dev/null 2>&1 &
  serial=""
  until [[ -n "$serial" ]]; do
    sleep 2
    serial=$("$ADB" devices | awk 'NR>1 && $1 ~ /^emulator-/ {print $1}' | grep -vxF "$before" | head -1 || true)
  done
  echo "Waiting for $serial to boot..."
  until [[ "$("$ADB" -s "$serial" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" == "1" ]]; do
    sleep 2
  done
else
  serial=${target#device:}
fi

export ANDROID_SERIAL="$serial"
./gradlew :app:installDebug "$@"
"$ADB" -s "$serial" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1 >/dev/null
echo "Launched $APP_ID on $serial"
