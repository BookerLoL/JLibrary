package main.designpattern.gameloop;

//https://gameprogrammingpatterns.com/game-loop.html#interview-with-a-cpu
/*
 * Platform event loop: simple, nice with platform, lose control over timing
 * Game Engine loop: don't have to write it and lose control
 * Write yourself: control, interface with platform
 * 
 * CPU Usage
 * - run fast: don't spare CPU, spare cycles improve FPS, best game exp
 * - Clamp frame rate: set upper limit on fps, rest, "good enough" exp
 * 
 * Control Gameplay
 * - Fixed time step with no synchonization
 * - Fixed time step with synchronization (allows double buffering)
 * - Variable time step (non-deterministic)
 * - Fixed update time step, variable rendering
 */
public class GameLoopTester {
	public static void main(String[] args) {
		
	}
	
	private static void testWithinFrame(final short MS_PER_FRAME) throws InterruptedException {
		while (true) {
			long start = System.currentTimeMillis();
			//processInput();
			//update();
			//render();
			Thread.sleep(System.currentTimeMillis() - start - MS_PER_FRAME);
		}
	}
	
	private static void testWithTimeSteps(final short MS_PER_FRAME) throws InterruptedException {
		double prev = System.currentTimeMillis();
		double lag = 0.0;
		while (true) {
			double current = System.currentTimeMillis();
			double elapsed = current - prev;
			prev = current;
			lag += elapsed;
			//processInput();
			while (lag >= MS_PER_FRAME) {
				//update();
				lag -= MS_PER_FRAME;
			}
			
			//render(lag / MS_PER_UPDATE);
		}
	}

	public static double msPerFrame(double fps) {
		return 1000 / fps;
	}
}
