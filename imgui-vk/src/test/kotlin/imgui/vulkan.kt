package imgui

import uno.glfw.GlfwWindow
import uno.glfw.glfw
import uno.glfw.windowHint.Api
import uno.vk.requiredInstanceExtensions
import uno.vk.vulkanSupported
import vkk.*
import vkk.entities.VkDebugReportCallback
import vkk.entities.VkDescriptorPool
import vkk.entities.VkPipelineCache
import vkk.extensions.VkDebugReport
import vkk.identifiers.Device
import vkk.identifiers.Instance
import vkk.identifiers.PhysicalDevice
import vkk.identifiers.Queue
import vkk.vk10.physicalDevices
import vkk.vk10.queueFamilyProperties
import vkk.vk10.structs.*
import kotlin.system.exitProcess

lateinit var instance: Instance
lateinit var physicalDevice: PhysicalDevice
lateinit var device: Device
var queueFamily = -1
lateinit var queue: Queue
var debugReport = VkDebugReportCallback.NULL
var pipelineCache = VkPipelineCache.NULL
var descriptorPool = VkDescriptorPool.NULL

//static ImGui_ImplVulkanH_Window g_MainWindowData;
//static int                      g_MinImageCount = 2;
//static bool                     g_SwapChainRebuild = false;
//static int                      g_SwapChainResizeWidth = 0;
//static int                      g_SwapChainResizeHeight = 0;

fun main() {

    // Setup GLFW window
    glfw.errorCallback = glfw.defaultErrorCallback
    glfw.init()

    glfw.windowHint {
        api = Api.None
    }
    val window = GlfwWindow(1280, 720, "Dear ImGui GLFW+Vulkan example")

    // Setup Vulkan
    if (!glfw.vulkanSupported) {
        System.err.println("GLFW: Vulkan Not Supported")
        exitProcess(1)
    }
    setupVulkan(glfw.requiredInstanceExtensions)

    // Create Window Surface
//    VkSurfaceKHR surface;
//    VkResult err = glfwCreateWindowSurface(g_Instance, window, g_Allocator, &surface);
//    check_vk_result(err);
//
//    // Create Framebuffers
//    int w, h;
//    glfwGetFramebufferSize(window, &w, &h);
//    glfwSetFramebufferSizeCallback(window, glfw_resize_callback);
//    ImGui_ImplVulkanH_Window* wd = &g_MainWindowData;
//    SetupVulkanWindow(wd, surface, w, h);
//
//    // Setup Dear ImGui context
//    IMGUI_CHECKVERSION();
//    ImGui::CreateContext();
//    ImGuiIO& io = ImGui::GetIO(); (void)io;
//    //io.ConfigFlags |= ImGuiConfigFlags_NavEnableKeyboard;     // Enable Keyboard Controls
//    //io.ConfigFlags |= ImGuiConfigFlags_NavEnableGamepad;      // Enable Gamepad Controls
//
//    // Setup Dear ImGui style
//    ImGui::StyleColorsDark();
//    //ImGui::StyleColorsClassic();
//
//    // Setup Platform/Renderer bindings
//    ImGui_ImplGlfw_InitForVulkan(window, true);
//    ImGui_ImplVulkan_InitInfo init_info = {};
//    init_info.Instance = g_Instance;
//    init_info.PhysicalDevice = g_PhysicalDevice;
//    init_info.Device = g_Device;
//    init_info.QueueFamily = g_QueueFamily;
//    init_info.Queue = g_Queue;
//    init_info.PipelineCache = g_PipelineCache;
//    init_info.DescriptorPool = g_DescriptorPool;
//    init_info.Allocator = g_Allocator;
//    init_info.MinImageCount = g_MinImageCount;
//    init_info.ImageCount = wd->ImageCount;
//    init_info.CheckVkResultFn = check_vk_result;
//    ImGui_ImplVulkan_Init(&init_info, wd->RenderPass);
//
//    // Load Fonts
//    // - If no fonts are loaded, dear imgui will use the default font. You can also load multiple fonts and use ImGui::PushFont()/PopFont() to select them.
//    // - AddFontFromFileTTF() will return the ImFont* so you can store it if you need to select the font among multiple.
//    // - If the file cannot be loaded, the function will return NULL. Please handle those errors in your application (e.g. use an assertion, or display an error and quit).
//    // - The fonts will be rasterized at a given size (w/ oversampling) and stored into a texture when calling ImFontAtlas::Build()/GetTexDataAsXXXX(), which ImGui_ImplXXXX_NewFrame below will call.
//    // - Read 'docs/FONTS.txt' for more instructions and details.
//    // - Remember that in C/C++ if you want to include a backslash \ in a string literal you need to write a double backslash \\ !
//    //io.Fonts->AddFontDefault();
//    //io.Fonts->AddFontFromFileTTF("../../misc/fonts/Roboto-Medium.ttf", 16.0f);
//    //io.Fonts->AddFontFromFileTTF("../../misc/fonts/Cousine-Regular.ttf", 15.0f);
//    //io.Fonts->AddFontFromFileTTF("../../misc/fonts/DroidSans.ttf", 16.0f);
//    //io.Fonts->AddFontFromFileTTF("../../misc/fonts/ProggyTiny.ttf", 10.0f);
//    //ImFont* font = io.Fonts->AddFontFromFileTTF("c:\\Windows\\Fonts\\ArialUni.ttf", 18.0f, NULL, io.Fonts->GetGlyphRangesJapanese());
//    //IM_ASSERT(font != NULL);
//
//    // Upload Fonts
//    {
//        // Use any command queue
//        VkCommandPool command_pool = wd->Frames[wd->FrameIndex].CommandPool;
//        VkCommandBuffer command_buffer = wd->Frames[wd->FrameIndex].CommandBuffer;
//
//        err = vkResetCommandPool(g_Device, command_pool, 0);
//        check_vk_result(err);
//        VkCommandBufferBeginInfo begin_info = {};
//        begin_info.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO;
//        begin_info.flags |= VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT;
//        err = vkBeginCommandBuffer(command_buffer, &begin_info);
//        check_vk_result(err);
//
//        ImGui_ImplVulkan_CreateFontsTexture(command_buffer);
//
//        VkSubmitInfo end_info = {};
//        end_info.sType = VK_STRUCTURE_TYPE_SUBMIT_INFO;
//        end_info.commandBufferCount = 1;
//        end_info.pCommandBuffers = &command_buffer;
//        err = vkEndCommandBuffer(command_buffer);
//        check_vk_result(err);
//        err = vkQueueSubmit(g_Queue, 1, &end_info, VK_NULL_HANDLE);
//        check_vk_result(err);
//
//        err = vkDeviceWaitIdle(g_Device);
//        check_vk_result(err);
//        ImGui_ImplVulkan_DestroyFontUploadObjects();
//    }
//
//    // Our state
//    bool show_demo_window = true;
//    bool show_another_window = false;
//    ImVec4 clear_color = ImVec4(0.45f, 0.55f, 0.60f, 1.00f);
//
//    // Main loop
//    while (!glfwWindowShouldClose(window))
//    {
//        // Poll and handle events (inputs, window resize, etc.)
//        // You can read the io.WantCaptureMouse, io.WantCaptureKeyboard flags to tell if dear imgui wants to use your inputs.
//        // - When io.WantCaptureMouse is true, do not dispatch mouse input data to your main application.
//        // - When io.WantCaptureKeyboard is true, do not dispatch keyboard input data to your main application.
//        // Generally you may always pass all inputs to dear imgui, and hide them from your application based on those two flags.
//        glfwPollEvents();
//
//        if (g_SwapChainRebuild)
//        {
//            g_SwapChainRebuild = false;
//            ImGui_ImplVulkan_SetMinImageCount(g_MinImageCount);
//            ImGui_ImplVulkanH_CreateWindow(g_Instance, g_PhysicalDevice, g_Device, &g_MainWindowData, g_QueueFamily, g_Allocator, g_SwapChainResizeWidth, g_SwapChainResizeHeight, g_MinImageCount);
//            g_MainWindowData.FrameIndex = 0;
//        }
//
//        // Start the Dear ImGui frame
//        ImGui_ImplVulkan_NewFrame();
//        ImGui_ImplGlfw_NewFrame();
//        ImGui::NewFrame();
//
//        // 1. Show the big demo window (Most of the sample code is in ImGui::ShowDemoWindow()! You can browse its code to learn more about Dear ImGui!).
//        if (show_demo_window)
//            ImGui::ShowDemoWindow(&show_demo_window);
//
//        // 2. Show a simple window that we create ourselves. We use a Begin/End pair to created a named window.
//        {
//            static float f = 0.0f;
//            static int counter = 0;
//
//            ImGui::Begin("Hello, world!");                          // Create a window called "Hello, world!" and append into it.
//
//            ImGui::Text("This is some useful text.");               // Display some text (you can use a format strings too)
//            ImGui::Checkbox("Demo Window", &show_demo_window);      // Edit bools storing our window open/close state
//            ImGui::Checkbox("Another Window", &show_another_window);
//
//            ImGui::SliderFloat("float", &f, 0.0f, 1.0f);            // Edit 1 float using a slider from 0.0f to 1.0f
//            ImGui::ColorEdit3("clear color", (float*)&clear_color); // Edit 3 floats representing a color
//
//            if (ImGui::Button("Button"))                            // Buttons return true when clicked (most widgets return true when edited/activated)
//                counter++;
//            ImGui::SameLine();
//            ImGui::Text("counter = %d", counter);
//
//            ImGui::Text("Application average %.3f ms/frame (%.1f FPS)", 1000.0f / ImGui::GetIO().Framerate, ImGui::GetIO().Framerate);
//            ImGui::End();
//        }
//
//        // 3. Show another simple window.
//        if (show_another_window)
//        {
//            ImGui::Begin("Another Window", &show_another_window);   // Pass a pointer to our bool variable (the window will have a closing button that will clear the bool when clicked)
//            ImGui::Text("Hello from another window!");
//            if (ImGui::Button("Close Me"))
//                show_another_window = false;
//            ImGui::End();
//        }
//
//        // Rendering
//        ImGui::Render();
//        memcpy(&wd->ClearValue.color.float32[0], &clear_color, 4 * sizeof(float));
//        FrameRender(wd);
//
//        FramePresent(wd);
//    }
//
//    // Cleanup
//    err = vkDeviceWaitIdle(g_Device);
//    check_vk_result(err);
//    ImGui_ImplVulkan_Shutdown();
//    ImGui_ImplGlfw_Shutdown();
//    ImGui::DestroyContext();
//
//    CleanupVulkanWindow();
//    CleanupVulkan();
//
//    glfwDestroyWindow(window);
//    glfwTerminate();
//
//    return 0;
}

//
//static void check_vk_result(VkResult err)
//{
//    if (err == 0) return;
//    printf("VkResult %d\n", err);
//    if (err < 0)
//        abort();
//}
//
//#ifdef IMGUI_VULKAN_DEBUG_REPORT
//static VKAPI_ATTR VkBool32 VKAPI_CALL debug_report(VkDebugReportFlagsEXT flags, VkDebugReportObjectTypeEXT objectType, uint64_t object, size_t location, int32_t messageCode, const char* pLayerPrefix, const char* pMessage, void* pUserData)
//{
//    (void)flags; (void)object; (void)location; (void)messageCode; (void)pUserData; (void)pLayerPrefix; // Unused arguments
//    fprintf(stderr, "[vulkan] ObjectType: %i\nMessage: %s\n\n", objectType, pMessage);
//    return VK_FALSE;
//}
//#endif // IMGUI_VULKAN_DEBUG_REPORT
//
fun setupVulkan(extensions: ArrayList<String>) {
//    VkResult err;

    // Create Vulkan Instance
    run {
        val createInfo = InstanceCreateInfo(enabledExtensionNames = extensions)

        if (DEBUG) {
            // Enabling multiple validation layers grouped as LunarG standard validation
            createInfo.enabledLayerNames = listOf("VK_LAYER_LUNARG_standard_validation")

            // Enable debug report extension (we need additional storage, so we duplicate the user array to add our new extension to it)
            extensions += "VK_EXT_debug_report"
            createInfo.enabledExtensionNames = extensions

            // Create Vulkan Instance
            instance = Instance(createInfo)

            // Setup the debug report callback
            val debugReportCi = DebugReportCallbackCreateInfo(flags = VkDebugReport.ERROR_BIT_EXT.i or VkDebugReport.WARNING_BIT_EXT.i or VkDebugReport.PERFORMANCE_WARNING_BIT_EXT.i)
            TODO()
//            debugReport = instance.createDebugReportCallbackEXT(debugReportCi)
        } else
        // Create Vulkan Instance without any debug feature
            instance = Instance(createInfo)
    }

    // Select GPU
    run {
        TODO()
//        val gpus = instance.physicalDevices

        // If a number >1 of GPUs got reported, you should find the best fit GPU for your purpose
        // e.g. VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU if available, or with the greatest memory available, etc.
        // for sake of simplicity we'll just take the first one, assuming it has a graphics queue family.
//        physicalDevice = gpus[0]
    }

    // Select graphics queue family
    run {
        TODO()
//        val queues = physicalDevice.queueFamilyProperties
//        for (i in queues.indices)
//            if (queues[i].queueFlags has VkQueueFlag.GRAPHICS_BIT) {
//                queueFamily = i
//                break
//            }
//        assert(queueFamily != -1)
    }

    // Create Logical Device (with 1 queue)
    run {
        TODO()
//        val queueInfo = DeviceQueueCreateInfo(
//                queueFamilyIndex = queueFamily,
//                queuePriority = 1f)
//        val createInfo = DeviceCreateInfo(
//                queueCreateInfo = queueInfo,
//                enabledExtensionNames = listOf("VK_KHR_swapchain"))
//        device = physicalDevice createDevice createInfo
//        queue = device.getQueue(queueFamily)
    }

    // Create Descriptor Pool
    run {
        TODO()
//        val poolSizes = arrayOf(
//                DescriptorPoolSize(VkDescriptorType.SAMPLER, 1000),
//                DescriptorPoolSize(VkDescriptorType.COMBINED_IMAGE_SAMPLER, 1000),
//                DescriptorPoolSize(VkDescriptorType.SAMPLED_IMAGE, 1000),
//                DescriptorPoolSize(VkDescriptorType.STORAGE_IMAGE, 1000),
//                DescriptorPoolSize(VkDescriptorType.UNIFORM_TEXEL_BUFFER, 1000),
//                DescriptorPoolSize(VkDescriptorType.STORAGE_TEXEL_BUFFER, 1000),
//                DescriptorPoolSize(VkDescriptorType.UNIFORM_BUFFER, 1000),
//                DescriptorPoolSize(VkDescriptorType.STORAGE_BUFFER, 1000),
//                DescriptorPoolSize(VkDescriptorType.UNIFORM_BUFFER_DYNAMIC, 1000),
//                DescriptorPoolSize(VkDescriptorType.STORAGE_BUFFER_DYNAMIC, 1000),
//                DescriptorPoolSize(VkDescriptorType.INPUT_ATTACHMENT, 1000))
//        val poolInfo = DescriptorPoolCreateInfo(
//                flags = VkDescriptorPoolCreate.FREE_DESCRIPTOR_SET_BIT.i,
//                maxSets = 1000 * poolSizes.size,
//                poolSizes = poolSizes)
//        descriptorPool = device createDescriptorPool poolInfo
    }
}
//
//// All the ImGui_ImplVulkanH_XXX structures/functions are optional helpers used by the demo.
//// Your real engine/app may not use them.
//static void SetupVulkanWindow(ImGui_ImplVulkanH_Window* wd, VkSurfaceKHR surface, int width, int height)
//{
//    wd->Surface = surface;
//
//    // Check for WSI support
//    VkBool32 res;
//    vkGetPhysicalDeviceSurfaceSupportKHR(g_PhysicalDevice, g_QueueFamily, wd->Surface, &res);
//    if (res != VK_TRUE)
//    {
//        fprintf(stderr, "Error no WSI support on physical device 0\n");
//        exit(-1);
//    }
//
//    // Select Surface Format
//    const VkFormat requestSurfaceImageFormat[] = { VK_FORMAT_B8G8R8A8_UNORM, VK_FORMAT_R8G8B8A8_UNORM, VK_FORMAT_B8G8R8_UNORM, VK_FORMAT_R8G8B8_UNORM };
//    const VkColorSpaceKHR requestSurfaceColorSpace = VK_COLORSPACE_SRGB_NONLINEAR_KHR;
//    wd->SurfaceFormat = ImGui_ImplVulkanH_SelectSurfaceFormat(g_PhysicalDevice, wd->Surface, requestSurfaceImageFormat, (size_t)IM_ARRAYSIZE(requestSurfaceImageFormat), requestSurfaceColorSpace);
//
//    // Select Present Mode
//    #ifdef IMGUI_UNLIMITED_FRAME_RATE
//        VkPresentModeKHR present_modes[] = { VK_PRESENT_MODE_MAILBOX_KHR, VK_PRESENT_MODE_IMMEDIATE_KHR, VK_PRESENT_MODE_FIFO_KHR };
//    #else
//    VkPresentModeKHR present_modes[] = { VK_PRESENT_MODE_FIFO_KHR };
//    #endif
//    wd->PresentMode = ImGui_ImplVulkanH_SelectPresentMode(g_PhysicalDevice, wd->Surface, &present_modes[0], IM_ARRAYSIZE(present_modes));
//    //printf("[vulkan] Selected PresentMode = %d\n", wd->PresentMode);
//
//    // Create SwapChain, RenderPass, Framebuffer, etc.
//    IM_ASSERT(g_MinImageCount >= 2);
//    ImGui_ImplVulkanH_CreateWindow(g_Instance, g_PhysicalDevice, g_Device, wd, g_QueueFamily, g_Allocator, width, height, g_MinImageCount);
//}
//
//static void CleanupVulkan()
//{
//    vkDestroyDescriptorPool(g_Device, g_DescriptorPool, g_Allocator);
//
//    #ifdef IMGUI_VULKAN_DEBUG_REPORT
//        // Remove the debug report callback
//        auto vkDestroyDebugReportCallbackEXT = (PFN_vkDestroyDebugReportCallbackEXT)vkGetInstanceProcAddr(g_Instance, "vkDestroyDebugReportCallbackEXT");
//    vkDestroyDebugReportCallbackEXT(g_Instance, g_DebugReport, g_Allocator);
//    #endif // IMGUI_VULKAN_DEBUG_REPORT
//
//    vkDestroyDevice(g_Device, g_Allocator);
//    vkDestroyInstance(g_Instance, g_Allocator);
//}
//
//static void CleanupVulkanWindow()
//{
//    ImGui_ImplVulkanH_DestroyWindow(g_Instance, g_Device, &g_MainWindowData, g_Allocator);
//}
//
//static void FrameRender(ImGui_ImplVulkanH_Window* wd)
//{
//    VkResult err;
//
//    VkSemaphore image_acquired_semaphore  = wd->FrameSemaphores[wd->SemaphoreIndex].ImageAcquiredSemaphore;
//    VkSemaphore render_complete_semaphore = wd->FrameSemaphores[wd->SemaphoreIndex].RenderCompleteSemaphore;
//    err = vkAcquireNextImageKHR(g_Device, wd->Swapchain, UINT64_MAX, image_acquired_semaphore, VK_NULL_HANDLE, &wd->FrameIndex);
//    check_vk_result(err);
//
//    ImGui_ImplVulkanH_Frame* fd = &wd->Frames[wd->FrameIndex];
//    {
//        err = vkWaitForFences(g_Device, 1, &fd->Fence, VK_TRUE, UINT64_MAX);    // wait indefinitely instead of periodically checking
//        check_vk_result(err);
//
//        err = vkResetFences(g_Device, 1, &fd->Fence);
//        check_vk_result(err);
//    }
//    {
//        err = vkResetCommandPool(g_Device, fd->CommandPool, 0);
//        check_vk_result(err);
//        VkCommandBufferBeginInfo info = {};
//        info.sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO;
//        info.flags |= VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT;
//        err = vkBeginCommandBuffer(fd->CommandBuffer, &info);
//        check_vk_result(err);
//    }
//    {
//        VkRenderPassBeginInfo info = {};
//        info.sType = VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO;
//        info.renderPass = wd->RenderPass;
//        info.framebuffer = fd->Framebuffer;
//        info.renderArea.extent.width = wd->Width;
//        info.renderArea.extent.height = wd->Height;
//        info.clearValueCount = 1;
//        info.pClearValues = &wd->ClearValue;
//        vkCmdBeginRenderPass(fd->CommandBuffer, &info, VK_SUBPASS_CONTENTS_INLINE);
//    }
//
//    // Record Imgui Draw Data and draw funcs into command buffer
//    ImGui_ImplVulkan_RenderDrawData(ImGui::GetDrawData(), fd->CommandBuffer);
//
//    // Submit command buffer
//    vkCmdEndRenderPass(fd->CommandBuffer);
//    {
//        VkPipelineStageFlags wait_stage = VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT;
//        VkSubmitInfo info = {};
//        info.sType = VK_STRUCTURE_TYPE_SUBMIT_INFO;
//        info.waitSemaphoreCount = 1;
//        info.pWaitSemaphores = &image_acquired_semaphore;
//        info.pWaitDstStageMask = &wait_stage;
//        info.commandBufferCount = 1;
//        info.pCommandBuffers = &fd->CommandBuffer;
//        info.signalSemaphoreCount = 1;
//        info.pSignalSemaphores = &render_complete_semaphore;
//
//        err = vkEndCommandBuffer(fd->CommandBuffer);
//        check_vk_result(err);
//        err = vkQueueSubmit(g_Queue, 1, &info, fd->Fence);
//        check_vk_result(err);
//    }
//}
//
//static void FramePresent(ImGui_ImplVulkanH_Window* wd)
//{
//    VkSemaphore render_complete_semaphore = wd->FrameSemaphores[wd->SemaphoreIndex].RenderCompleteSemaphore;
//    VkPresentInfoKHR info = {};
//    info.sType = VK_STRUCTURE_TYPE_PRESENT_INFO_KHR;
//    info.waitSemaphoreCount = 1;
//    info.pWaitSemaphores = &render_complete_semaphore;
//    info.swapchainCount = 1;
//    info.pSwapchains = &wd->Swapchain;
//    info.pImageIndices = &wd->FrameIndex;
//    VkResult err = vkQueuePresentKHR(g_Queue, &info);
//    check_vk_result(err);
//    wd->SemaphoreIndex = (wd->SemaphoreIndex + 1) % wd->ImageCount; // Now we can use the next set of semaphores
//}
//
//
//static void glfw_resize_callback(GLFWwindow*, int w, int h)
//{
//    g_SwapChainRebuild = true;
//    g_SwapChainResizeWidth = w;
//    g_SwapChainResizeHeight = h;
//}
//
