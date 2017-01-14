/* Create confirmation dialogs. Requires that alertify was loaded */

/** Create a normal confirmation
  * title      - Title text
  * message    - Body text
  * okText     - Ok button text
  * cancelText - Cancel button text
  * fOk        - Function to be called if ok is pressed
  */
function confirm(title, message, okText, cancelText, fOk) {
    alertify.confirm(title, message, fOk, function() { }).set(
        'labels', {ok: okText, cancel: cancelText}
    );
}
