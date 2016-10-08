package webCore.controllers.homeChooser

import javax.inject.Inject

import play.Configuration

/**
 * HomeChooser implementation that finds the home route from the application's
 * config file.
 * It expects a home.route property that contains the path and returns that
 */
class HomeChooserConfig @Inject() (
  val config: Configuration
) extends HomeChooser {
  override val home = config.getString("home.route")
}
