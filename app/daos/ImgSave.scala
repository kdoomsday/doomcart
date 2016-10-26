package daos

import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart


/** Save product images to a location */
trait ImgSave {
  type Url = String

  /** Permanently save an image for a product
    * @param productId Product's id
    * @param filePart  Image to save
    * @return Url for the image
    */
  def save(productId: Long, filePart: FilePart[TemporaryFile]): Url;
}
