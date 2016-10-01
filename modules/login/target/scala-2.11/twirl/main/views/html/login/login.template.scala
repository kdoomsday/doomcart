
package views.html.login

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object login_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class login extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.1*/("""<h1>Login page</h1>

<form action="#">
    <input type="text" name="login" placeholder="login">
    <input type="password" name="password" placeholder="password">
</form>"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


}

/**/
object login extends login_Scope0.login
              /*
                  -- GENERATED --
                  DATE: Fri Sep 30 21:51:40 VET 2016
                  SOURCE: /opt/sbtworkspace/doomcart/modules/login/app/views/login/login.scala.html
                  HASH: 24b2cc9a72105210fef27b1b2274fb6d5efebebb
                  MATRIX: 615->0
                  LINES: 25->1
                  -- GENERATED --
              */
          