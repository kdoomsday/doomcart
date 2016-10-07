package webCore.controllers.homeChooser

/** Injectable home chooser so that implementations can decide where home is */
trait HomeChooser {
  /** URI to teh home page */
  def home: String
}
