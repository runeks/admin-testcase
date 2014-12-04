describe('EVA.View.RegisterModifiedBallot', function () {

    it('is available', function () {
        expect(EVA.View.RegisterModifiedBallot).not.toBe(null);
    });

    it('changes checkbox value when enter is pressed', function () {
        var changeValueOfCandidateCheckBoxSpy = spyOn(EVA.View.RegisterModifiedBallot.prototype, "changeValueOfCandidateCheckBox");
        var clearDirectToCandidateFieldSpy = spyOn(EVA.View.RegisterModifiedBallot.prototype, "clearDirectToCandidateField");

        var eventFixture = {
            keyCode: 13,
            preventDefault: function () {
            },
            stopImmediatePropagation: function () {
            }
        };
        var view = new EVA.View.RegisterModifiedBallot();
        view.changeCheckBoxValueIfEnterIsPressed(eventFixture);

        expect(changeValueOfCandidateCheckBoxSpy).toHaveBeenCalled();
        expect(clearDirectToCandidateFieldSpy).toHaveBeenCalled();
    });

    it('does not change checkbox value when enter is not pressed', function () {
        var changeValueOfCandidateCheckBoxSpy = spyOn(EVA.View.RegisterModifiedBallot.prototype, "changeValueOfCandidateCheckBox");
        var clearDirectToCandidateFieldSpy = spyOn(EVA.View.RegisterModifiedBallot.prototype, "clearDirectToCandidateField");

        var eventFixture = {
            keyCode: 32,
            preventDefault: function () {
            },
            stopImmediatePropagation: function () {
            }
        };
        var view = new EVA.View.RegisterModifiedBallot();
        view.changeCheckBoxValueIfEnterIsPressed(eventFixture);

        expect(changeValueOfCandidateCheckBoxSpy).not.toHaveBeenCalled();
        expect(clearDirectToCandidateFieldSpy).not.toHaveBeenCalled();
    });

});
