/*
 *	DEMO HELPERS
 */


/**
 *	debugData
 *
 *	Pass me a data structure {} and I'll output all the key/value pairs - recursively
 *
 *	@example var HTML = debugData( oElem.style, "Element.style", { keys: "top,left,width,height", recurse: true, sort: true, display: true, returnHTML: true });	
 *
 *	@param Object	o_Data   A JSON-style data structure
 *	@param String	s_Title  Title for dialog (optional)
 *	@param Hash	options  Pass additional options in a hash
 */
function debugData (o_Data, s_Title, options) {
	options = options || {};
	var
		str=s_Title || 'DATA'
	//	maintain backward compatibility with OLD 'recurseData' param
	,	recurse=(typeof options=='boolean' ? options : options.recurse !==false)
	,	keys=(options.keys?','+options.keys+',':false)
	,	display=options.display !==false
	,	html=options.returnHTML !==false
	,	sort=options.sort !==false
	,	D=[], i=0 // Array to hold data, i=counter
	,	hasSubKeys = false
	,	k, skip, x	// loop vars
	;
	parse(o_Data,''); // recursive parsing

	str+='\n***'+'****************************'.substr(0,str.length);
	if (sort && !hasSubKeys) D.sort(); // sort by keyName - but NOT if has subKeys!
	str+='\n'+ D.join('\n'); // add line-breaks

	if (display) alert(str); // display data
	if (html) str=str.replace(/\n/g, ' <br>').replace(/  /g, ' &nbsp;'); // format as HTML
	return str;

	function parse ( data, prefix ) {
		if (typeof prefix=='undefined') prefix='';
		try {
			$.each( data, function (key, val) {
				k = prefix+key+':  ';
				skip = (keys && keys.indexOf(','+key+',') == -1);
				if (typeof val=='function') { // FUNCTION
					if (!skip) D[i++] = k +'function()';
				}
				else if (typeof val=='string') { // STRING
					if (!skip) D[i++] = k +'"'+ val +'"';
				}
				else if (typeof val !='object') { // NUMBER or BOOLEAN
					if (!skip) D[i++] = k + val;
				}
				else if (isArray(val)) { // ARRAY
					if (!skip) D[i++] = k +'[ '+ val.toString() +' ]'; // output delimited array
				}
				else { // JSON
					if (!recurse || !hasKeys(val)) { // show an empty hash
						if (!skip) D[i++] = k +'{ }';
					}
					else { // recurse into JSON hash - indent output
						D[i++] = k +'{';
						parse( val, prefix+'    '); // RECURSE
						D[i++] = prefix +'}';
					}
				}
			});
		} catch (e) {}
		function isArray(o) {
			return (o && typeof o==='object' && !o.propertyIsEnumerable('length') && typeof o.length==='number');
		}
		function hasKeys(o) {
			var c=0;
			for (x in o) c++;
			if (!hasSubKeys) hasSubKeys = !!c;
			return !!c;
		}
	}
};


/**
 *	showOptions
 *
 *	Pass a layout-options object, and the pane/key you want to display
 */
function showOptions (o_Settings, key) {
	var data = o_Settings.options;
	$.each(key.split("."), function() {
		data = data[this]; // resurse through multiple levels
	});
	debugData( data, 'options.'+key );
};

/**
 *	showState
 *
 *	Pass a layout-options object, and the pane/key you want to display
 */
function showState (o_Settings, key) {
	debugData( o_Settings.state[key], 'state.'+key );
};


/**
 *	addThemeSwitcher
 *
 *	Remove the cookie set by the UI Themeswitcher to reset a page to default styles
 *
 *	Dependancies: /lib/js/themeswitchertool.js
 */
function addThemeSwitcher ( container, position ) {
	var
		con	= 'body'
	,	pos = { top: '10px', right: '10px' }
	;
	$('<div style="position: absolute; overflow-x: hidden;" />')
		.css( position || pos )
		.appendTo( container || con)
		.themeswitcher()
	;
};

/**
 *	removeUITheme
 *
 *	Remove the cookie set by the UI Themeswitcher to reset a page to default styles
 */
function removeUITheme ( cookieName, removeCookie ) {
	$('link.ui-theme').remove();
	$('.jquery-ui-themeswitcher-title').text( 'Switch Theme' );
	if (removeCookie !== false)
		$.cookie( cookieName || 'jquery-ui-theme', null );
};

