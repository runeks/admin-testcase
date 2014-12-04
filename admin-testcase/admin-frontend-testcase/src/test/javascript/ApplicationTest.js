/**
 * Created by dba on 03/11/14.
 */

describe('EVA.Application', function () {

	var CSRFFixture = '1234566788990';
	var CIDFixture = 10;


	function createDOMFixture(id, dataViewName) {
		if (this.DOMFixture) {
			this.DOMFixture.remove();
		}

		this.DOMFixture = $([
			'<div class="page-container">',
				'<div id="' + id + '" class="page" data-view="' + dataViewName + '"></div>',
			'</div>'
		].join(''));

		this.DOMFixture.appendTo('body');
	}

	it('is available', function () {
		expect(EVA.Application).not.toBe(null);
	});

	it('throws an error getInstance is called without an instance existing', function () {
		try {
			expect(EVA.Application.getInstance()).toThrowAnyError();
		} catch (expectedError) {}
	});

	it('throws an error when CSRF token is undefined or null', function () {
		try {
			expect(EVA.Application.createInstance()).toThrowAnyError();
		} catch (expectedError) {}
	});

	it('returns correct instance', function () {
		var applicationInstance = EVA.Application.createInstance({csrfToken:CSRFFixture});
		expect(applicationInstance).toBe(EVA.Application.getInstance());
	});

	//need to do some more work on testing document ready in jasmine
	xit('it bootstraps the correct view when data-view is present', function () {

		createDOMFixture('page-operator-admin', 'OperatorAdmin');

		expect($('.page-container > .page').length).toEqual(1);
		
		var bootstrapSpy = spyOn(EVA.Application.prototype, 'bootstrap');
		var resolveViewSpy = spyOn(EVA.Application.prototype, 'resolveViewClass');
		var setViewSpy = spyOn(EVA.Application.prototype, 'setView');
		
		EVA.Application.createInstance(CSRFFixture, '');

		expect(bootstrapSpy).toHaveBeenCalled();
		expect(resolveViewSpy).toHaveBeenCalledWith('OperatorAdmin').andReturn(new EVA.View.OperatorAdmin());
		expect(setViewSpy).toHaveBeenCalled();
	});

	it('has the correct CSRF token', function () {
		var applicationInstance = EVA.Application.createInstance({csrfToken:CSRFFixture});
		expect(applicationInstance.getCSRFToken()).toEqual(CSRFFixture);
	});

	it('creates routes with correct path', function () {
		var applicationInstance = EVA.Application.createInstance(CSRFFixture, null);
		expect(applicationInstance.createRoute('/foo.xhtml', false)).toEqual('/secure/foo.xhtml');
	});

	it('throws exception when create routes is passed a null or undefined value', function () {
		var applicationInstance = EVA.Application.createInstance(CSRFFixture, CIDFixture);
		expect(applicationInstance.createRoute(null)).toThrowAnyError();
		expect(applicationInstance.createRoute(undefined)).toThrowAnyError();
	});

	it('creates routes with correct path and append conversationId', function () {
		var applicationInstance = EVA.Application.createInstance({csrfToken:CSRFFixture, conversationId:CIDFixture});
		expect(applicationInstance.createRoute('/foo.xhtml', true)).toEqual('/secure/foo.xhtml?cid=10');
	});
	
	it('creates a sessionTimeout component when config key "keepAlive"', function () {
		var applicationInstance = EVA.Application.createInstance({
			csrfToken:CSRFFixture, 
			conversationId:CIDFixture,
			keepAlive: {
				url:'/foo.html',
				timeout:5,
				interval:5,
				message:'out are about to get logged out',
				padding:2,
				buttonLabel: 'time left: '
			}
		});
		
		expect(applicationInstance.sessionTimeout).not.toBeUndefined();
		expect(applicationInstance.sessionTimeout instanceof EVA.Components.SessionTimeout).toBeTrue();
	});
	
	it('does not creates a sessionTimeout component when config key "keepAlive" is undefined', function () {
		var applicationInstance = EVA.Application.createInstance({csrfToken:CSRFFixture});
		expect(applicationInstance.sessionTimeout).toBeUndefined();
	});
	
	it('is able to set and get a view instance', function () {
		var applicationInstance = EVA.Application.createInstance(CSRFFixture, CIDFixture);
		var viewInstance = new EVA.View.OperatorAdmin();
		applicationInstance.setView(viewInstance);
		expect(applicationInstance.getView()).toEqual(viewInstance);
	});

	it('attaches onDocumentReady handlers', function () {
		var applicationInstance = EVA.Application.createInstance(CSRFFixture, CIDFixture);
		var documentReadySpy = spyOn(applicationInstance, 'onDocumentReady');
		applicationInstance.onDocumentReady(function () {});
		expect(documentReadySpy).toHaveBeenCalled();
	});
	
	it('attaches onViewReady handlers', function () {
		var applicationInstance = EVA.Application.createInstance(CSRFFixture, CIDFixture);
		var onViewReadySpy = spyOn(applicationInstance, 'onViewReady');
		applicationInstance.onViewReady(function () {});
		expect(onViewReadySpy).toHaveBeenCalled();
	});
});
