import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import Groq from "groq-sdk";

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

const groq = new Groq({
  apiKey: process.env.GROQ_API_KEY,
});

app.get("/", (req, res) => {
  res.json({ message: "LLM ChatBot backend is running" });
});

app.post("/chat", async (req, res) => {
  try {
    const { username, message } = req.body;

    if (!message || message.trim() === "") {
      return res.status(400).json({
        reply: "Please enter a message.",
      });
    }

    const completion = await groq.chat.completions.create({
      model: "llama-3.1-8b-instant",
      messages: [
        {
          role: "system",
          content:
            "You are a helpful chatbot inside a university Android assignment app. Keep responses clear, friendly, and concise.",
        },
        {
          role: "user",
          content: `${username || "User"} says: ${message}`,
        },
      ],
    });

    const reply =
      completion.choices?.[0]?.message?.content ||
      "Sorry, I could not generate a response.";

    res.json({ reply });
  } catch (error) {
    console.error("Groq API error:", error);

    res.status(500).json({
      reply: "Sorry, I could not get a response. Please try again.",
    });
  }
});

app.listen(PORT, () => {
  console.log(`Backend server running on http://localhost:${PORT}`);
});