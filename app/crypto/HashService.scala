package crypto

/**
  * User: Eduardo Barrientos
  * Date: 15/10/16
  * Time: 05:23 PM
  */
trait HashService {

  /**
    * @param s    Plaintext String
    * @param salt Salt to use for hashing
    * @return Bytes produced
    */
  def hash(s: String, salt: Int): Array[Byte]

  final def hashString(s: String, salt: Int): String = HashService.toHexString(hash(s, salt))
}

object HashService {
  /** Convert an array of bytes to a corresponding hex string */
  def toHexString(bytes: Array[Byte]): String = {
    (for {
      b <- bytes
      hex = (b & 0xFF) + 0x100
      hexed = Integer.toHexString(hex).tail
    } yield hexed)
        .mkString
  }
}
