cmake_minimum_required(VERSION 3.4.1)

project(leak-tracer)

set(CMAKE_VERBOSE_MAKEFILE on)
message("Native Leak tracer lib compile begin......")
message(STATUS ${CMAKE_SOURCE_DIR})
message(STATUS ${PROJECT_SOURCE_DIR})

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../lib)
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../lib)

include_directories(libleaktracer/include)
add_library( # Sets the name of the library.
        leaktracer

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        leaktracer_jni.cpp
        Object.cpp
        libleaktracer/src/AllocationHandlers.cpp
        libleaktracer/src/MemoryTrace.cpp
        )

find_library( # Sets the name of the path variable.
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        leaktracer
        ${log-lib})

file(COPY ${PROJECT_SOURCE_DIR}/../lib/libleaktracer.so
        DESTINATION  ${PROJECT_SOURCE_DIR}/../../../../leak-tracer-helpers )