
jQuery(document).ready(function() {
	setTimeout(function() {
	    YAHOO.example.ColumnShowHide = function() {
	    	
	    	var dataTableID = jQuery(".yui-dt").parent().attr("id");
			
	    	dataTableID = dataTableID.replace(':', '_');
			dataTableID = dataTableID.replace(':', '_');
			dataTableID = "widget_" + dataTableID;
			
	        // Create DataTable j_idt21_j_id3_widget
			var myDataTable = eval(dataTableID);
	        var myDataSource = myDataTable.getDataSource();
	        
	        // Enables Column selection
	        myDataTable.subscribe("theadCellDblclickEvent",  function (oArgs) {
	       
	        	if (this.getColumn(oArgs.target).selected == true) {
	        		this.unselectColumn(this.getColumn(oArgs.target));
	        		
	        		if (myDataTable.getSelectedColumns().length == 1) {
	        	  		var removeColumn = myDataTable.getColumn(myDataTable.getColumnSet().keys.length - 1);
	            		myDataTable.removeColumn(removeColumn);
	        	  	}
	        	}
	        	else if (myDataTable.getSelectedColumns().length < 2) {
	        		this.selectColumn(this.getColumn(oArgs.target));
	        	
		            if (myDataTable.getSelectedColumns().length == 2) {
		            	var column1 = myDataTable.getSelectedColumns()[1];
		            	var column2 = myDataTable.getSelectedColumns()[2];
		            	var newColumn = new YAHOO.widget.Column({ key: "diff", label: "Diff" });
		            	
		            	myDataTable.subscribe("postRenderEvent",  function () {
		            		diff();
		            	});
		            	myDataTable.insertColumn(newColumn, myDataTable.getColumnSet().keys.length);
		            } 
	            }
	        }); 
	        
	        // Shows dialog, creating one when necessary
	        var newCols = true;
	        var showDlg = function(e) {
	            YAHOO.util.Event.stopEvent(e);
	            if(newCols) {
	                // Populate Dialog
	                // Using a template to create elements for the SimpleDialog
	                var allColumns = myDataTable.getColumnSet().keys;
	                var elPicker = YAHOO.util.Dom.get("dt-dlg-picker");
	                var elTemplateCol = document.createElement("div");
	                YAHOO.util.Dom.addClass(elTemplateCol, "dt-dlg-pickercol");
	                var elTemplateKey = elTemplateCol.appendChild(document.createElement("span"));
	                YAHOO.util.Dom.addClass(elTemplateKey, "dt-dlg-pickerkey");
	                var elTemplateBtns = elTemplateCol.appendChild(document.createElement("span"));
	                YAHOO.util.Dom.addClass(elTemplateBtns, "dt-dlg-pickerbtns");
	                var onclickObj = {fn:handleButtonClick, obj:this, scope:false };
	           
	                // Create one section in the SimpleDialog for each Column
	                var elColumn, elKey, elButton, oButtonGrp;
	                for(var i=0,l=allColumns.length;i<l;i++) {
	                  
		                var oColumn = allColumns[i];
		                if(oColumn.getKey() != "rowIndex" && oColumn.getKey() != "diff") {  
		                    // Use the template
		                    elColumn = elTemplateCol.cloneNode(true);
		                    
		                    // Write the Column key
		                    elKey = elColumn.firstChild;
		                    elKey.innerHTML = oColumn.label;
		                    
		                    // Create a ButtonGroup
		                    oButtonGrp = new YAHOO.widget.ButtonGroup({ 
		                                    id: "buttongrp"+i, 
		                                    name: oColumn.getKey(), 
		                                    container: elKey.nextSibling
		                    });
		                    oButtonGrp.addButtons([
		                        { label: "Show", value: "Show", checked: ((!oColumn.hidden)), onclick: onclickObj},
		                        { label: "Hide", value: "Hide", checked: ((oColumn.hidden)), onclick: onclickObj}
		                    ]);
		                                    
		                    elPicker.appendChild(elColumn);
	                    }
	                }
	                newCols = false;
	        	}
	            myDlg.show();
	        };
	        var hideDlg = function(e) {
	            this.hide();
	        };
	        var handleButtonClick = function(e, oSelf) {
	            var sKey = this.get("name");
	            if(this.get("value") === "Hide") {
	                // Hides a Column
	                myDataTable.hideColumn(sKey);
	            }
	            else {
	                // Shows a Column
	                myDataTable.showColumn(sKey);
	            }
	        };
	        
	        // Create the SimpleDialog
	        YAHOO.util.Dom.removeClass("dt-dlg", "inprogress");
	        var myDlg = new PrimeFaces.widget.Dialog('dt-dlg', {
	        	visible:false,
	        	fixedcenter: true,
	        	constrainToViewport: true,
	        	buttons: [ 
					{ text:"Close",  handler:hideDlg }
		        ],
	        	effect:{
	        		effect:YAHOO.widget.ContainerEffect.FADE, 
	        		duration: 0.5
	        	}
	        });
	     
			myDlg.render();
	        // Nulls out myDlg to force a new one to be created
	        myDataTable.subscribe("columnReorderEvent", function(){
	            newCols = true;
	            YAHOO.util.Event.purgeElement("dt-dlg-picker", true);
	            YAHOO.util.Dom.get("dt-dlg-picker").innerHTML = "";
	        }, this, true);
			
			// Hook up the SimpleDialog to the link
			YAHOO.util.Event.addListener("dt-options-link", "click", showDlg, this, true);
			
			return {
			  oDS: myDataSource,
			  oDT: myDataTable
			};
	    }();
	    function diff() { 
	    	var var1 = [];
    		
    		jQuery("td.yui-dt-selected").each(function(i,selected) {
    			var1[i] = jQuery(selected);
        	  });
    		
    		var j = 0;
        	jQuery("td.yui-dt-col-diff").each(function() {
        		var value1 = jQuery(var1[j]).children(":first").children(":first").val();
        		var value2 = jQuery(var1[j+=1]).children(":first").children(":first").val();
        		
        		jQuery(this).text(value1-value2);
        	
        		if(value1-value2 < 0) {
        			jQuery(this).css('color','red');
        		} else {
        			jQuery(this).css('color','');
        		}
        		j+=1;
        	});
	    }
	    // jQuery("td div input").change( function() {
		jQuery('td div input').livequery('change', function(event) { 
	    	if (jQuery(this).parent().parent().hasClass("yui-dt-selected") && jQuery(this).parent().parent().siblings(".yui-dt-selected").length == 1) {
	    		diff();
	    	}
	    });
	}, 100); 
});