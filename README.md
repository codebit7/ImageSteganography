Image Steganography Android App
Overview
Image Steganography is an Android application that allows users to hide secret messages within images using steganography techniques. The app provides a secure and intuitive interface to encode messages with password protection, retrieve images from the internet, generate AI-based images, and manage user profiles. Built with modern Android development practices, it leverages Firebase for authentication and data storage, Retrofit for network operations, and a TabLayout with fragments for seamless navigation.
Features

Message Encoding/Decoding: Encode secret messages into images from mobile storage with password protection and decode them securely.
Internet Image Retrieval: Fetch images from the internet using the Retrofit library for use in steganography.
AI Image Generation: Generate images using AI for creative steganography applications.
Navigation: Four fragments with a TabLayout:
Home: Main interface for encoding/decoding operations.
Search Images: Search and retrieve images from the internet.
Generate from AI: Create AI-generated images.
Profile: View and manage user profile data.


Authentication: Secure user authentication and profile management using Firebase Authentication and Firestore.
Profile Storage: Store user profile data in Firebase Firestore for a personalized experience.

Tech Stack

Language: Kotlin/Java
Architecture: MVVM (Model-View-ViewModel)
Libraries:
Retrofit: For fetching images from the internet.
Firebase Authentication: For secure user login.
Firebase Firestore: For storing user profile data.
TabLayout & ViewPager2: For fragment-based navigation.


UI Components: Material Design, Fragments, RecyclerView
AI Integration: AI-based image generation (specific API or model to be specified).
Dependencies: AndroidX, Google Material Components

Installation

Clone the Repository:git clone https://github.com/codebit7/ImageSteganography.git


Open in Android Studio:
Open Android Studio and select Open an existing project.
Navigate to the cloned repository folder and select it.


Configure Firebase:
Create a Firebase project in the Firebase Console.
Add your Android app to the project and download the google-services.json file.
Place the google-services.json file in the app/ directory.
Enable Firebase Authentication (Email/Password) and Firestore in the Firebase Console.


Sync and Build:
Sync the project with Gradle by clicking Sync Project with Gradle Files.
Build and run the app on an emulator or physical device (API 21 or higher).



Usage

Sign Up/Login: Create an account or log in using Firebase Authentication.
Encode a Message:
Navigate to the Home fragment.
Select an image from device storage or use an AI-generated/internet-fetched image.
Enter a secret message and a password.
Encode the message into the image and save the output.


Decode a Message:
Select the encoded image in the Home fragment.
Enter the password to decode and retrieve the hidden message.


Search Images: Use the Search Images fragment to fetch images from the internet.
Generate AI Images: Create unique images in the Generate from AI fragment.
Manage Profile: View and update profile information in the Profile fragment.

Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a new branch (git checkout -b feature/your-feature).
Make your changes and commit (git commit -m 'Add your feature').
Push to the branch (git push origin feature/your-feature).
Open a Pull Request.

License
This project is licensed under the MIT License. See the LICENSE file for details.
Contact
For inquiries or feedback, reach out to:

GitHub: codebit7
Email: wamiqrahim@gmail.com

