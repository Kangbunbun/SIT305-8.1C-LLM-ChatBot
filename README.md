# SIT305 Task 8.1C - LLM ChatBot App

This project is an Android chatbot application developed for SIT305 Task 8.1C.

The app allows users to enter a username, open a chat screen, send messages to an AI chatbot, and view previous chat messages.

The LLM integration is handled through a local Node.js backend. The Android app does not store the API key directly.

## Main Features

- Username login screen
- Chat screen with user and chatbot message bubbles
- AI-generated chatbot responses
- Local chat history saved using SQLite
- Timestamp displayed on each message bubble
- User-based chat history loading
- Basic loading/error handling for chatbot requests
- Backend-based LLM integration to keep the API key outside the Android app

## LLM ChatBot Functionality

This app includes one main LLM-powered chatbot feature.

### AI Chat Response

On the chat screen, the user can type a message and tap Send.

The app sends the username and message to the local backend. The backend forwards the message to the LLM API and returns an AI-generated reply.

The Android app then displays the chatbot response in the chat interface and saves both the user message and chatbot response into the local SQLite database.

If the backend is not available, the app displays an error message instead of crashing.

## Technology Stack

### Android

- Kotlin
- XML layouts
- ViewBinding
- RecyclerView
- SQLiteOpenHelper
- Retrofit
- Coroutines

### Backend

- Node.js
- Express
- Groq API
- dotenv

## Backend Setup

The backend is required for real AI chatbot responses.

Install backend dependencies:

```powershell
npm install
```

Create a `.env` file based on `.env.example`:

```env
GROQ_API_KEY=your_groq_api_key_here
PORT=3000
```

Start the backend:

```powershell
npm start
```

The backend should run at:

```text
http://localhost:3000
```

You can test the backend with:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:3000/" `
  -Method GET
```

Expected response:

```text
LLM ChatBot backend is running
```

You can also test the chatbot endpoint with:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:3000/chat" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"Khang","message":"Explain what a chatbot is in one simple sentence."}'
```

## Android Setup

Open the Android project in Android Studio and run the app on an emulator.

The Android emulator connects to the local backend using:

```text
http://10.0.2.2:3000/
```

This URL is configured in:

```text
app/src/main/java/com/example/chatbot/data/remote/RetrofitClient.kt
```

## Running the App

Start the backend first:

```powershell
npm install
npm start
```

Then open the Android project in Android Studio and run the app on an emulator.

Use the app flow:

```text
Login -> Chat Screen -> Send Message -> Receive AI Response
```

To test chat history:

```text
Login with a username -> Send messages -> Close the app -> Open the app again -> Login with the same username
```

The previous chat messages should be loaded from SQLite.

## Security Note

The real API key should only be stored in:

```text
.env
```

This file is ignored by Git and should not be uploaded to GitHub.

Only this example file is included in the repository:

```text
.env.example
```

## Author

Thien Khang Nguyen
