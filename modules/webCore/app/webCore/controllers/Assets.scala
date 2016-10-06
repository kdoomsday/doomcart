package webCore.controllers

import play.api.http.HttpErrorHandler
import javax.inject._

/**
  * User: Eduardo Barrientos
  * Date: 05/10/16
  * Time: 09:25 PM
  */
class Assets @Inject() (errorHandler: HttpErrorHandler) extends controllers.AssetsBuilder(errorHandler)
