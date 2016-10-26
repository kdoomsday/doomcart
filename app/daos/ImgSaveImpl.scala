package daos

import java.io.File
import java.util.UUID
import javax.inject.Inject

import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import play.api.Configuration


class ImgSaveImpl @Inject() (configuration: Configuration) extends ImgSave {
  val root = new File(configuration.getString("daos.imgSaveImpl.root").getOrElse(System.getProperty("java.io.tmpdir")))

  // make sure root dir exists
  if (!root.exists) root.mkdirs()

  override def save(productId: Long, filePart: FilePart[TemporaryFile]): Url = {
    val subdir = new File(root, productId.toString())
    val dest = new File(subdir, UUID.randomUUID() + filePart.filename)
    dest.mkdirs()
    filePart.ref.moveTo(dest, true)
    dest.toURI().toURL().toString()
  }
}
