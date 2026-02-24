# Financial Planning Agent

A Spring Boot application that uses Google's Agent Development Kit (ADK) to create a financial planning agent. This agent can analyze a user's portfolio and provide recommendations based on their risk tolerance.

## Features

- **Agent-based Architecture**: Uses Google ADK for agent orchestration
- **LLM Integration**: Powered by Open AI
- **Portfolio Analysis**: Analyzes stock portfolios for concentration risks
- **Tool Usage**: Implements custom tools for risk assessment and diversification
- **REST API**: Exposes endpoints for agent interaction

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Google Gemini API Key

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FinancialPlanningAgents
   ```

2. **Configure API Key**
   Create a `.env` file (if it doesn't exist) and add your OpenAI API key:
   ```properties
   OPENAI.api-key=your-openai-api-key-here
   ```

## Build

Build the project using Maven:

```bash
mvn clean package
```

## Run

Run the application using Spring Boot:

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## Docker

Build the Docker image:

```bash
docker build -t financial-planner-agent .
```

Run the Docker container:

```bash
docker run -p 8080:8080 financial-planner-agent
```

## API Endpoints

### Chat with Agent

```http
POST /agent/chat
Content-Type: application/json

{
  "request": "What is my portfolio risk level?"
}
```

**Response:**

```json
{
  "response": "Your portfolio risk level is 4. You have a high concentration in AAPL..."
}
```

## Tools

The agent uses the following custom tools:

- **ConcentratedPosition**: Detects concentrated stock positions
- **Diversification**: Recommends diversification strategies
- **RiskScoreCalculator**: Calculates portfolio risk score

## License
