# Othello 12x12 AI Bot

Welcome to the repository for the Othello 12x12 AI bot, developed as a project for the CSCI 561 Foundations of Artificial Intelligence Course at USC. This project achieved the 1st rank among a class of 216 students in the course competition, showcasing exceptional performance.

## About the Project

In this project, I developed an Othello AI bot based on the Min-Max algorithm with Alpha-Beta pruning. The goal was to create an intelligent bot capable of making strategic moves to outperform opponents within a limited time frame.

### Achievements

- Ranked 1st in a class of 216 students in the CSCI 561 course competition.
- Developed an efficient AI bot capable of competing against diverse strategies within a time limit of 300 seconds per bot for all available moves.

### Approach

Implemented Mini-Max algorithm with Alpha–beta pruning pruning from scratch.

1. **Focus on Heuristic Tuning:**
   - Created a referee within this repository to monitor matches between different versions of the AI bot with varying heuristics and depths.
   - Determined the best heuristics through rigorous testing to ensure fast performance and high victory rates.

2. **Conditional Heuristics:**
   - Implemented conditional heuristics based on the board state inside the mini-max tree.
   - Specialized heuristics are applied exclusively to terminal states in order to address unique game conditions, such as the significance of piece count, which may not be relevant during intermediate stages.

3. **Increase Depth:**
   - Enhanced the bot's performance to increase search depth within time limit.
   - Implemented sorting techniques and traversal ordering techniques to improve Alpha-beta pruning and consequently increase depth levels without compromising on time efficiency.

## Project Structure

- root: Contains the source code for the Othello AI bot.
- `match/`: Includes the referee program used to monitor matches between AI bots.
- `match/bot1/`: Includes bot1 code run by refree
- `match/bot2`: Includes bot2 code run by refree

## Demo Video



## Getting Started

To try out the Othello AI bot or explore the source code, follow these steps:

1. Clone this repository to your local machine.
2. Navigate to the folder `match/`.
3. Run the batch script run_match.bat to try out match between Bot1 and Bot2.
4. Try out different depths and heuristics with the bots inside `match/bot1` and `match/bot2`

## Support and Feedback

If you have any questions, suggestions, or feedback, feel free to reach out to me! I welcome contributions and am open to collaboration opportunities.

## License

This project is licensed under the MIT License.

---

Thank you for your interest in the Othello 12x12 AI Bot project! I hope you find it informative and inspiring. Happy gaming!



