describe "directives.api.Window", ->
    beforeEach(->
        ### Possible Attributes
                coords: '=coords',
				show: '=show',
				templateUrl: '=templateurl',
				templateParameter: '=templateparameter',
				isIconVisibleOnClick: '=isiconvisibleonclick',
				closeClick: '&closeclick',           #scope glue to gmap InfoWindow closeclick
				options: '=options'
        ###
        @mocks =
            scope:
                coords:
                    latitude: 90.0
                    longitude: 89.0
                show: true
                $watch:()->
                $on:()->
            element:
                html: ->
                    "test html"
            attrs: {}
            ctrls: [
                {getMap:()->{}}
            ]


        @timeOutNoW = (fnc,time) =>
            fnc()

        @gMarker = new google.maps.Marker(@commonOpts)
        angular.module('mockModule', [])
        .controller 'windowDirective', ($timeout, $compile, $http, $templateCache) =>
            new directives.api.Window(@timeOutNoW,$compile, $http, $templateCache)

        angular.mock.module('mockModule')

        inject(($timeout, $compile, $http, $templateCache, $controller) =>
            @subject = $controller('windowDirective', $timeout, $compile, $http, $templateCache)
            @subject.onChildCreation = (child) =>
                @childWindow = child
        )
    )

    it 'can be created', ->
        expect(@subject).toBeDefined()
        expect(@subject.index).toEqual(@index)

    describe "link", ->
        #link: (scope, element, attrs, ctrls) =>
        describe "withOUT marker", ->
            it 'link creates window options and a childWindow', ->
#                runs ->
                @subject.link(@mocks.scope, @mocks.element, @mocks.attrs, @mocks.ctrls)
#                waitsFor =>
#                    @childWindow?
#                , 3000
#                runs ->
                expect(@childWindow).toBeDefined()
                expect(@childWindow.opts).toBeDefined()

        describe "with marker", ->
