# 📱 MeetUp – Connect with Like-Minded People

MeetUp is an Android application designed to help users find and connect with people who share common interests. Whether it's photography, fitness, food, art, or travel, MeetUp enables users to discover and chat with others nearby based on shared passions.

---

## 📌 Abstract

**MeetUp** is a social platform where people with similar interests can meet, interact, and share their thoughts through an in-app messaging service. The application allows users to:

- Discover and join groups based on common interests.
- Connect with individuals within a 5-mile radius.
- Access news across various interest categories.
- Chat instantly with like-minded users.

By encouraging networking and community building, MeetUp helps users expand their social circle and stay informed.

---

## 🧩 Introduction

**MeetUp** is developed in **Android Studio** using **Java** for the backend and **Google Firebase** for database and user authentication. It is a platform for:

- Networking
- Exploring interests
- Joining/Creating groups
- Sharing thoughts

The app focuses on building a local community of like-minded individuals.

---

## 🔍 Existing System

While platforms like Facebook, Instagram, and YouTube foster global connectivity, MeetUp addresses **local community building** and **interest-based social interaction**. It offers:

- Latest news updates  
- Chatting features  
- Interest-based group creation  
- Personalized content sharing  

---

## 🏗️ Proposed Method & Architecture

Upon registration and phone verification, users access the main app features:

- **News Activity**: Displays categorized news (7 types)
- **Profile Activity**: Update profile info and interests
- **Messenger Activity**: Connect with like-minded users or all users

Architecture follows a **multi-activity** model with Firebase handling real-time messaging and user data.

---

## ⚙️ Methodology

- **MainActivity**: Displays categorized news; each post opens in a WebView.
- **ProfileActivity**: Lets users update personal details and interests.
- **List of Like-Minded Users**: Enables chatting with people having similar interests.
- **List of All Users**: View and chat with any registered user.
- **WebViewActivity**: News posts are displayed in full.
- **ChatActivity**: Allows real-time chatting and sharing of links.

---

## 🛠️ Technologies Used

- **Java**
- **Android Studio**
- **Firebase Realtime Database**
- **Firebase Authentication**

---

## 📱 Requirements

- Android API level 24 (Android 7.0) or higher  
- Android SDK  
- Internet connection  

### Permissions Required

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
