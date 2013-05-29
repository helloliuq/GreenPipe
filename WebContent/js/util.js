function lookUpCookie(Name) {
  var search = Name + "="
  var returnvalue = undefined;
  if (document.cookie.length > 0) {
    offset = document.cookie.indexOf(search)
    // if cookie exists
    if (offset != -1) { 
      offset += search.length
      // set index of beginning of value
      end = document.cookie.indexOf(";", offset);
      // set index of end of cookie value
      if (end == -1) end = document.cookie.length;
      returnvalue=unescape(document.cookie.substring(offset, end))
      }
   }
  return returnvalue;
}
/** function showOneSection(toshow) {
        var sections = [ 'main', 'approval', 'waiting' ];
        for (var i=0; i < sections.length; ++i) {
          var s = sections[i];
          var el = $(s);
          if (s === toshow) {
            el.style.display = "block";
          } else {
            el.style.display = "none";
          }
        }
} **/


Function.prototype.inherits = function(parentCtor) {
  function tempCtor() {};
  tempCtor.prototype = parentCtor.prototype;
  this.superClass_ = parentCtor.prototype;
  this.prototype = new tempCtor();
  this.prototype.constructor = this;
};
