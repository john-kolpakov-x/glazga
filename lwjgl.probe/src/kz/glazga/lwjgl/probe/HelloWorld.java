package kz.glazga.lwjgl.probe;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class HelloWorld {

  // The window handle
  private long window;

  public void run() {
    System.out.println("Hello LWJGL `" + Version.getVersion() + "`");

    init();
    loop();

    // Free the window callbacks and destroy the window
    Callbacks.glfwFreeCallbacks(window);
    GLFW.glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    GLFW.glfwTerminate();
    GLFWErrorCallback glfwErrorCallback = GLFW.glfwSetErrorCallback(null);
    if (glfwErrorCallback != null) {
      glfwErrorCallback.free();
    }
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!GLFW.glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Configure GLFW
    GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
    GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    window = GLFW.glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    GLFW.glfwSetKeyCallback(window, (window, key, scanCode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
      }
    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      GLFW.glfwGetWindowSize(window, pWidth, pHeight);

      long monitor = GLFW.glfwGetPrimaryMonitor();

      // Get the resolution of the primary monitor
      GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
      requireNonNull(vidMode);

      // Center the window
      GLFW.glfwSetWindowPos(
        window,
        (vidMode.width() - pWidth.get(0)) / 2,
        (vidMode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    GLFW.glfwMakeContextCurrent(window);
    // Enable v-sync
    GLFW.glfwSwapInterval(1);

    // Make the window visible
    GLFW.glfwShowWindow(window);
  }

  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    // Set the clear color
    GL11.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(-3.2, 3.2, -2.4, 2.4, -1, 1);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!GLFW.glfwWindowShouldClose(window)) {
      GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


      // Begin drawing
      GL11.glBegin(GL11.GL_TRIANGLES);
      // Top & Red
      GL11.glColor3f(1.0f, 0.0f, 0.0f);
      GL11.glVertex2f(0.0f, 1.0f);

      // Right & Green
      GL11.glColor3f(0.0f, 1.0f, 0.0f);
      GL11.glVertex2f(1.0f, 1.0f);

      // Left & Blue
      GL11.glColor3f(0.0f, 0.0f, 1.0f);
      GL11.glVertex2f(1.0f, -1.0f);
      GL11.glEnd();


      GLFW.glfwSwapBuffers(window); // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      GLFW.glfwPollEvents();
    }
  }

  public static void main(String[] args) {
    new HelloWorld().run();
  }

}
