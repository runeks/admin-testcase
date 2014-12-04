jQuery(document).ready(function(){
	$ = jQuery;
	$('.confirmCategorySelection').parent().each(function(){
		var confirmCategorySelection = $(this).find('.confirmCategorySelection');
		confirmCategorySelection.find('input').change(function(){
			$(this).parent().parent('td').find('input[type=radio]').data('subChecked', this.checked);
		});
		$(this).find('input[type=radio]').change(function(){
			confirmCategorySelection.slideUp(100).find('input').get(0).checked=$(this).data('subChecked');
		});
		$(this).find('input[type=radio]').not('[value=0]').change(function(){
			var self = this;

			if( $(this).get(0).checked ) setTimeout(function(){
				confirmCategorySelection.appendTo($(self).parent().parent()).slideDown(250);
			}, 100);
		});
	});
	$('table.radioList input[type=radio]').change(function(){
		$(this).parents('table.radioList').find('td').removeClass('selected');
		$(this).parent().addClass('selected');
	}).each(function(){
		if($(this).get(0).checked) {
			var checkBox = $(this).parents('table.radioList').parent().find('.confirmCategorySelection input');
			if(checkBox.length>0) {
				$(this).data('subChecked', checkBox.get(0).checked)
			}
			$(this).change();
		}
	});
});