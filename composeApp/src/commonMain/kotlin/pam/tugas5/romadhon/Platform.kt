package pam.tugas5.romadhon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform