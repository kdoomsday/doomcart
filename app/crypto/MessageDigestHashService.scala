package crypto

import java.nio.charset.{Charset, StandardCharsets}
import java.security.MessageDigest

/** Hash service that uses a MessageDigest to generate the hash. The hash type is selected at
  * initialization and can't be changed afterwards.
  * The available algorithms are exactly the same as for java.security.MessageDigest.
  * For converting to/from an array of bytes, a charset is used. By default this is UTF-8 but it can
  * be specified.
  * User: Eduardo Barrientos
  * Date: 15/10/16
  * Time: 05:29 PM
  */
class MessageDigestHashService(
    algorithm: String,
    charset: Charset = StandardCharsets.UTF_8
) extends HashService {

  val md = MessageDigest.getInstance(algorithm)


  override def hash(s: String, salt: Int): Array[Byte] = {
    val text: Array[Byte] = (s + salt).getBytes(charset)
    md.digest(text)
  }
}


object MessageDigestHashService {
  object Sha256HashService extends MessageDigestHashService("SHA-256", StandardCharsets.UTF_8)
}
