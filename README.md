# SIT305 8.1C LLM ChatBot App

This is an Android chatbot application created for SIT305 Credit Task 8.1C.

The app allows a user to enter a username, open a chat screen, send messages to an AI chatbot, and view previous chat messages. The project uses a small Node.js backend to connect the Android app to an LLM API, so the API key is not stored inside the Android code.

## Main Features

- Username login screen
- Chat interface with user and chatbot message bubbles
- AI chatbot response through a backend API
- Chat history saved locally using SQLite
- Timestamp shown on each message bubble
- Basic error handling when the backend is not available

## Project Structure

```text
SIT305-8.1C-LLM-ChatBot/
│
├── app/                    # Android app source code
├── backend/
│   └── server.js           # Node.js backend server
├── .env.example            # Example environment variables
├── package.json            # Backend dependencies and start script
└── README.md
```

## Technologies Used

### Android

- Kotlin
- XML Layouts
- RecyclerView
- SQLiteOpenHelper
- Retrofit
- Coroutines
- ViewBinding

### Backend

- Node.js
- Express
- dotenv
- Groq SDK

## How It Works

1. The user enters a username on the login screen.
2. The app opens the chat screen.
3. The user sends a message.
4. The Android app sends the message to the backend.
5. The backend sends the message to the LLM API.
6. The LLM response is returned to the Android app.
7. The user message and chatbot response are saved in SQLite.
8. When the same username logs in again, the previous chat history is loaded.

## Backend Setup

Install backend dependencies:

```powershell
npm install
```

Create a local `.env` file:

```powershell
copy .env.example .env
```

Open `.env` and add your own Groq API key:

```env
GROQ_API_KEY=your_real_groq_api_key_here
PORT=3000
```

Start the backend:

```powershell
npm start
```

If the backend is running correctly, it should show:

```text
Backend server running on http://localhost:3000
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

1. Open the project in Android Studio.
2. Make sure the backend is running on port 3000.
3. Run the app using an Android emulator.
4. The Android app connects to the backend using:

```kotlin
http://10.0.2.2:3000/
```

`10.0.2.2` is used because the Android emulator needs this address to access `localhost` on the computer.

## Security Note

The real API key must be stored only in the local `.env` file.

The `.env` file is ignored by Git and should not be pushed to GitHub. Only `.env.example` is included so another user can understand which environment variables are required.

## Testing Checklist

Before recording the demonstration video, I tested that:

- The backend starts successfully.
- The `/chat` endpoint returns an AI response.
- The Android app opens the login screen.
- A user can enter a username and open the chat screen.
- The user can send messages to the chatbot.
- The chatbot returns real AI-generated responses.
- User and chatbot messages display timestamps.
- Chat history is saved and loaded again when logging in with the same username.
- The app shows an error message instead of crashing if the backend is unavailable.

## Notes

This project is a simple academic implementation for the SIT305 8.1C task. The focus is on meeting the core requirements clearly: username login, chatbot interaction, local chat history, timestamps, and safe backend-based LLM integration.
