package controllers

import java.io.File
import javax.inject.Inject
import play.api.Configuration
import play.api.mvc.{ Action, Controller }


class ImageServer @Inject() (config: Configuration) extends Controller {
  val root = new File(
               config.getString("daos.imgSaveImpl.root")
                 .getOrElse(System.getProperty("java.io.tmpdir"))
             )

  /** Serve a file (image in this case) */
  def image(name: String) = Action {
    Ok.sendFile( new File(root, name) )
  }

}
