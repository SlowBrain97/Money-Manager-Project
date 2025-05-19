/* eslint-disable @typescript-eslint/no-explicit-any */
// src/utils/opencv.ts

// Track loading state
let isLoading = false;
let loadPromise: Promise<any> | null = null;

declare global {
  interface Window {
    cv: any;
    onOpenCVReady?: () => void;
  }
}

export function loadOpenCV(): Promise<any> {
  // Return existing promise if already loading
  if (loadPromise) {
    return loadPromise;
  }
  
  // Return immediately if already loaded
  if (window.cv) {
    return Promise.resolve(window.cv);
  }
  
  // Set loading flag and create new promise
  isLoading = true;
  loadPromise = new Promise((resolve, reject) => {
    // Check if script is already in DOM
    const existingScript = document.querySelector('script[src*="opencv.js"]');
    if (existingScript) {
      // Script already exists, just wait for it
      window.onOpenCVReady = () => {
        isLoading = false;
        console.log('OpenCV.js is ready!');
        resolve(window.cv);
      };
      return;
    }
    
    // Create script element if not exists
    const script = document.createElement('script');
    script.setAttribute('async', 'true');
    script.setAttribute('src', 'https://docs.opencv.org/4.7.0/opencv.js');
    script.setAttribute('id', 'opencv-script');
    
    // Set up callback for when OpenCV is ready
    window.onOpenCVReady = () => {
      isLoading = false;
      console.log('OpenCV.js is ready!');
      if (window.cv) {
        resolve(window.cv);
      } else {
        reject(new Error('OpenCV is not available'));
      }
    };

    // Handle errors
    script.onerror = () => {
      isLoading = false;
      loadPromise = null;
      reject(new Error('Failed to load OpenCV.js'));
    };

    // Add script to document
    document.body.appendChild(script);
  });
  
  return loadPromise;
}

export function getCV(): any {
  if (!window.cv) {
    throw new Error('OpenCV is not loaded. Call loadOpenCV() first.');
  }
  return window.cv;
}