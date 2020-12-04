package com.mechanitis.demo.spock

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class ExampleSpecification extends Specification {

    @Subject
    Polygon theTestSubject = new Polygon(56)

    def "should demonstrate a simple assertion"() {
        expect:
        1 == 1
    }

    // can use just when-then if there's no setup
    def "should demonstrate given-when-then"() {
        given:
        def shape = new Polygon(4)

        when:
        int sides = shape.getNumberOfSides()

        then:
        sides == 4
    }

    def "should expect Exceptions"() {
        when:
        new Polygon(0)

        then:
        // no need for .class
        def e = thrown(TooFewSidesException)
        // no need for get
        e.numberOfSides == 0
    }

    @Unroll
    def "should demonstrate simple data driven testing. Number of sides: #expected"() {
        expect:
        shape.getNumberOfSides() == expected

        where:
        expected << [3, 4, 5, 8, 14]
        shape = new Polygon(expected)
    }

    def "should demonstrate data tables. Max of #a and #b should be #c"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b || c
        1 | 3 || 3
        7 | 4 || 7
        0 | 0 || 0
    }

    def "should be able to mock a concrete class"() {
        given:
        Renderer renderer = Mock()
        @Subject
        def shape = new Polygon(4, renderer)

        when:
        shape.draw()

        then:
        4 * renderer.drawLine()
    }

    def "should be able to use a stub"() {
        given:
        Palette palette = Stub()
        palette.getPrimaryColour() >> Colour.Red
        @Subject
        def renderer = new Renderer(palette)

        expect:
        renderer.getForegroundColour() == Colour.Red
    }

    def "should demonstrate helper methods"() {
        given:
        def renderer = Mock(Renderer)
        def shapeFactory = new ShapeFactory(renderer)

        when:
        def shape = shapeFactory.createDefaultPolygon()

        then:
        checkDefaultShape(shape, renderer)
    }

    private static void checkDefaultShape(Polygon shape, Renderer renderer) {
        assert shape.numberOfSides == 4
        assert shape.renderer == renderer
    }
}
