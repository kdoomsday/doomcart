package crypto

import java.nio.charset.{Charset, StandardCharsets}

import core.crypto.{HashService, MessageDigestHashService}
import org.scalacheck.Properties

/**
  * User: Eduardo Barrientos
  * Date: 15/10/16
  * Time: 05:37 PM
  */
class TestMessageDigestHash extends Properties("MessageDigestHash") {

  def testObject(hashService: HashService, text: String, salt: Int, expected: String): Boolean = {
    val hashed = HashService.toHexString(hashService.hash(text, salt))
    hashed == expected
  }

  def testHash(algo: String, text: String, salt: Int, expected: String): Boolean =
    testObject(new MessageDigestHashService(algo, StandardCharsets.UTF_8), text, salt, expected)

  property("sha1") =
    testHash("SHA-1", "doomsday", 42, "ae681caf7e77dea8122fc8317b01ec6ce7f1d4a9")

  property("sha256") =
    testHash("SHA-256", "doomsday", 42, "2c6422132efd4172db0351b38882f809959313eb9a97efe21f5bd3ca66af379d")


  property("sha256Service") =
    testObject(MessageDigestHashService.Sha256HashService, "doomsday", 42, "2c6422132efd4172db0351b38882f809959313eb9a97efe21f5bd3ca66af379d")
}
