#!/usr/bin/env bash
# Dijalankan cron tiap menit di server. Kalau ada commit baru di origin/main,
# kode di-pull lalu container di-build ulang. Pakai --force untuk deploy paksa.
#
# Pasang di crontab:
#   * * * * * $HOME/apps/pbo-java/deploy/auto-deploy.sh >> $HOME/apps/pbo-java-deploy.log 2>&1

set -euo pipefail

APP_DIR="${APP_DIR:-$HOME/apps/pbo-java}"
BRANCH="${BRANCH:-main}"
export DB_PROPERTIES="${DB_PROPERTIES:-$HOME/pbo-java-db/db.properties}"

# Semua isi skrip dibungkus fungsi supaya bash membacanya utuh dulu.
# Penting, karena git reset bisa mengganti file skrip ini saat sedang berjalan.
main() {
    # Cegah dua deploy berjalan bersamaan kalau build lebih lama dari 1 menit
    exec 9>/tmp/pbo-java-deploy.lock
    flock -n 9 || exit 0

    cd "$APP_DIR"
    git fetch -q origin "$BRANCH"

    local current target
    current=$(git rev-parse HEAD)
    target=$(git rev-parse "origin/$BRANCH")

    if [[ "$current" == "$target" && "${1:-}" != "--force" ]]; then
        exit 0
    fi

    echo "[$(date '+%F %T')] Deploy ${current:0:7} -> ${target:0:7}"
    git reset -q --hard "origin/$BRANCH"

    # Container lama tetap jalan sampai image baru selesai di-build.
    # Kalau build gagal, versi lama tetap online.
    if docker compose up -d --build --remove-orphans; then
        docker image prune -f > /dev/null
        echo "[$(date '+%F %T')] Selesai: $(git log -1 --format='%h %s')"
    else
        echo "[$(date '+%F %T')] GAGAL build ${target:0:7}, versi lama masih jalan" >&2
        exit 1
    fi
}

main "$@"
exit
