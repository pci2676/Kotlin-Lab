package com.javabom.bomkotlin.chap3

class SplitInfo(
    val directory: String,
    val fullName: String,
    val fileName: String,
    val extension: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SplitInfo

        if (directory != other.directory) return false
        if (fullName != other.fullName) return false
        if (fileName != other.fileName) return false
        if (extension != other.extension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = directory.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + extension.hashCode()
        return result
    }
}

fun parsePath(path: String): SplitInfo {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")

    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")

    return SplitInfo(directory, fullName, fileName, extension)
}

fun parsePath2(path: String): SplitInfo {
    val regex =
        """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path) ?: throw IllegalArgumentException("$path : 잘못된 파일 경로입니다.")
    val (directory, fileName, extension) = matchResult.destructured
    return SplitInfo(directory, "$fileName.$extension", fileName, extension)
}
