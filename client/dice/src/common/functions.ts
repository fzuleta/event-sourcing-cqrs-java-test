export function trace(o) {
    if (typeof console === "undefined") {
        //console not available
    } else {
        if (typeof o === "string") {
            console.log("%c"+o, 'background: #f8eafc; color: #302207');
        } else {
            console.log(o);
        }
    }
}