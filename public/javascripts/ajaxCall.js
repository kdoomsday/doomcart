/* Lib for calling something with an ID and getting a response */

/* Bring up confirmation dialog and generate an ajax call if user chooses yes.
 * Call is a 'post' call. The only parameter passed is an id.
 * route - Url for ajax call
 * id    - Id to pass on to post call
 * title - Confirm dialog title
 * text  - Confirm dialog text
 * okText - Confirm dialog ok button text
 * cancelText - Confirm dialog cancel button text
 * okMsg      - Message to display if everything was ok
 * errorMsg   - Error message in case something failed
 * fok        - Function to call on ok response
 */
function ajaxCall(route, id, title, text, okText, cancelText, okMsg, errorMsg, fok) {
    // Confirm and call ifConfirm() when necessary
    alertify.confirm(
        title,
        text,
        function() { ifConfirm(route, id, okMsg, errorMsg, fok); },
        function() { }
    ).set(
        'labels', {ok: okText, cancel: cancelText}
    );
}

/* Calls route, sending "id". Expects a response of "ok"; anything else is an
 * error
 */
function ifConfirm(callRoute, id, okMsg, errorMsg, fok) {
    $.ajax({
        url:      callRoute,
        method:   'post',
        data:     { "id": id },
        dataType: "text",
        success:  function(data, textStatus, jqXHR) {
            if (data == "ok") {
                fok();
                alertify.success(okMsg);
            }
            else {
                alertify.error(errorMsg);
            }
        },
        error:    function(jqXHR, textStatus, errorThrown) {
            alertify.error('General error');  // TODO Maybe do something about this?
        }
    });
}
