package daos

import java.io.File
import play.api.mvc.MultipartFormData


class ImgSaveImpl extends ImgSave {
  val root = new File("images/")

  // make sure root dir exists
  if (!root.exists) root.mkdirs()

  def save(filePart: MultipartFormData[File]): Url = {
    ???
  }
}
