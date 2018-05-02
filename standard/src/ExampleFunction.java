public class ExampleFunction implements Function {
	public double evaluate(final int x, final int y) {
		final double nx = (x - 35) / 40.0;
		final double ny = (y - 35) / 40.0;
		final double r = Math.sqrt(nx * nx + ny * ny);
		final double phi = Math.atan2(ny, nx);
		return 9.3 * Math.sin(5.5 * Math.PI * Math.pow(r, 0.2) + phi);
	}
}

