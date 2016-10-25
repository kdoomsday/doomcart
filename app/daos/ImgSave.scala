package daos

import java.io.File
import play.api.mvc.MultipartFormData


/** Save product images to a location */
trait ImgSave {
  type Url = String

  def save(filePart: MultipartFormData[File]): Url;
}
