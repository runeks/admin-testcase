describe('EVA.View.LocalConfiguration', function () {

	it('is available', function () {
		expect(EVA.View.LocalConfiguration).not.toBe(null);
	});

	it('calls initialize upon instantiation', function () {
		var initializeSpy = spyOn(EVA.View.LocalConfiguration.prototype, 'initialize');
		var view = new EVA.View.LocalConfiguration();

		expect(initializeSpy).toHaveBeenCalled();
	});

	it('return correct tabViewWidget', function () {
		var widgetSpy = spyOn(EVA.View.LocalConfiguration.prototype, 'widget');
		var view = new EVA.View.LocalConfiguration();
		view.getTabViewWidget();

		expect(widgetSpy).toHaveBeenCalledWith('tabViewWidget');
	});

	it('Unrecognized tabs throws error', function () {
		var view = new EVA.View.LocalConfiguration();
		var getTabViewSpy = spyOn(EVA.View.LocalConfiguration.prototype, 'getTabViewWidget').and.returnValue({
			getLength: function(){
				return 3;
			},
			cfg: {}
		});
		try {
			expect(view.onTabChange(0, 99)).toThrowAnyError();
			expect(getTabViewSpy).toHaveBeenCalled();
		} catch(error) {}
		
	});

	it('redirects to local area configuration tab without cid', function () {

		var redirectSpy = spyOn(EVA.Application.prototype, 'redirect');
		var getTabViewSpy = spyOn(EVA.View.LocalConfiguration.prototype, 'getTabViewWidget').and.returnValue({
			getLength: function(){
				return 3;
			},
			cfg: {}
		});
		var applicationInstance = new EVA.Application.createInstance('1231312313');
		var view = new EVA.View.LocalConfiguration();

		view.onTabChange(99,EVA.View.LocalConfiguration.AREA_TAB_INDEX);
		expect(getTabViewSpy).toHaveBeenCalled();
		expect(redirectSpy).toHaveBeenCalledWith('/local_area_configuration.xhtml', false);

	});

	it('redirects to local election configuration tab without cid', function () {

		var redirectSpy = spyOn(EVA.Application.prototype, 'redirect');
		var getTabViewSpy = spyOn(EVA.View.LocalConfiguration.prototype, 'getTabViewWidget').and.returnValue({
			getLength: function(){
				return 3;
			},
			cfg: {}
		});
		var applicationInstance = new EVA.Application.createInstance('1231312313');
		var view = new EVA.View.LocalConfiguration();
		
		view.onTabChange(99,EVA.View.LocalConfiguration.ELECTION_TAB_INDEX);
		expect(getTabViewSpy).toHaveBeenCalled();
		expect(redirectSpy).toHaveBeenCalledWith('/local_election_configuration.xhtml', false);
	});
	
	it('returns false when the active tab is clicked', function(){;
		var redirectSpy = spyOn(EVA.Application.prototype, 'redirect');
		var getTabViewSpy = spyOn(EVA.View.LocalConfiguration.prototype, 'getTabViewWidget').and.returnValue({
			getLength: function(){
				return 3;
			},
			cfg: {}
		});
		var view = new EVA.View.LocalConfiguration();
		var applicationInstance = new EVA.Application.createInstance('1231312313');
		var tabChangeResult = view.onTabChange(EVA.View.LocalConfiguration.ELECTION_TAB_INDEX,EVA.View.LocalConfiguration.ELECTION_TAB_INDEX);
		expect(tabChangeResult).toBeFalse();
		expect(getTabViewSpy).toHaveBeenCalled();
		expect(redirectSpy).not.toHaveBeenCalled();
	});
});
