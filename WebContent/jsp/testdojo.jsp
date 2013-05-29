<html>
<body>
<!--
<style type="text/css">
  @import "/dojotoolkit/dojox/grid/_grid/Grid.css";
  @import "/dojotoolkit/dojox/grid/_grid/nihiloGrid.css";
</style>
 http://archive.dojotoolkit.org/nightly/dojotoolkit/dojox/grid/tests/test_data_grid_edit.html
 -->

<script type="text/javascript">
  dojo.require("dojox.grid.Grid");
  dojo.require("dojox.grid._data.model");
  dojo.require("dojox.grid._data.dijitEditors");
  dojo.require("dojo.parser");
</script>
<script type="text/javascript" src="/dojotoolkit/dojox/grid/tests/support/test_data.js"></script>
<script type="text/javascript">
    // ==========================================================================
    // Create a data model
    // ==========================================================================

    s = (new Date()).getTime();

    data = [
      [ "normal", false, "new", 'But are not followed by two hexadecimal', 29.91, 10, false, s],
      [ "important", false, "new", 'Because a % sign always indicates', 9.33, -5, false, s ],
      [ "important", false, "read", 'Signs can be selectively', 19.34, 0, true, s ],
      [ "note", false, "read", 'However the reserved characters', 15.63, 0, true, s ],
      [ "normal", false, "replied", 'It is therefore necessary', 24.22, 5.50, true, s ],
      [ "important", false, "replied", 'To problems of corruption by', 9.12, -3, true, s ],
      [ "note", false, "replied", 'Which would simply be awkward in', 12.15, -4, false, s ]
    ];
    var rows = 100;
    for(var i=0, l=data.length; i<rows; i++){
      data.push(data[i%l].slice(0));
    }
    model = new dojox.grid.data.Table(null, data);
    // ==========================================================================
    // Tie some UI to the data model
    // ==========================================================================
    model.observer(this);
    modelChange = function(){
      dojo.byId("rowCount").innerHTML = 'Row count: ' + model.count;
    }
    /*
    modelInsertion = modelDatumChange = function(a1, a2, a3){
      console.debug(a1, a2, a3);
    }
    */
    // ==========================================================================
    // Custom formatters
    // ==========================================================================
    formatCurrency = function(inDatum){
      return isNaN(inDatum) ? '...' : dojo.currency.format(inDatum, this.constraint);
    }
    formatDate = function(inDatum){
      return dojo.date.locale.format(new Date(inDatum), this.constraint);
    }
    // ==========================================================================
    // Grid structure
    // ==========================================================================
    statusCell = {
      field: 2, name: 'Status',
      styles: 'text-align: center;',
      editor: dojox.grid.editors.Select,
      options: [ "new", "read", "replied" ]
    };

    gridLayout = [{
      type: 'dojox.GridRowView', width: '20px'
    },{
      defaultCell: { width: 8, editor: dojox.grid.editors.Input, styles: 'text-align: right;'  },
      rows: [[
        { name: 'Id',
          get: function(inRowIndex) { return inRowIndex+1;},
          editor: dojox.grid.editors.Dijit,
          editorClass: "dijit.form.NumberSpinner" },
        { name: 'Date', width: 10, field: 7,
          editor: dojox.grid.editors.DateTextBox,
          formatter: formatDate,
          constraint: {formatLength: 'long', selector: "date"}},
        { name: 'Priority', styles: 'text-align: center;', field: 0,
          editor: dojox.grid.editors.ComboBox,
          options: ["normal", "note", "important"], width: 10},
        { name: 'Mark', width: 3, styles: 'text-align: center;',
          editor: dojox.grid.editors.CheckBox},
        statusCell,
        { name: 'Message', styles: '', width: '100%',
          editor: dojox.grid.editors.Editor, editorToolbar: true },
        { name: 'Amount', formatter: formatCurrency, constraint: {currency: 'EUR'},
          editor: dojox.grid.editors.Dijit, editorClass: "dijit.form.CurrencyTextBox" },
        { name: 'Amount', field: 4, formatter: formatCurrency, constraint: {currency: 'EUR'},
          editor: dojox.grid.editors.Dijit, editorClass: "dijit.form.HorizontalSlider", width: 10}
      ]]
    }];
    // ==========================================================================
    // UI Action
    // ==========================================================================
    addRow = function(){
      grid2.addRow([
        "normal", false, "new",
        'Now is the time for all good men to come to the aid of their party.',
        99.99, 9.99, false
      ]);
    }
</script>

<div id="controls">
  <button dojoType="dijit.form.Button" onclick="grid2.refresh()">Refresh</button>
  <button dojoType="dijit.form.Button" onclick="grid2.edit.focusEditor()">Focus Editor</button>
  <button dojoType="dijit.form.Button" onclick="grid2.focus.next()">Next Focus</button>
  <button dojoType="dijit.form.Button" onclick="addRow()">Add Row</button>
  <button dojoType="dijit.form.Button" onclick="grid2.removeSelectedRows()">Remove</button>
  <button dojoType="dijit.form.Button" onclick="grid2.edit.apply()">Apply</button>
  <button dojoType="dijit.form.Button" onclick="grid2.edit.cancel()">Cancel</button>
  <button dojoType="dijit.form.Button" onclick="grid2.singleClickEdit = !grid.singleClickEdit">Toggle singleClickEdit</button>
</div>
<br />
<div id="grid2" jsId="grid2" dojoType="dojox.Grid" model="model" structure="gridLayout"
  style="width:650px;height:350px;border: 1px solid silver;"></div>
<br />
<div id="rowCount"></div>
</body>
</html>