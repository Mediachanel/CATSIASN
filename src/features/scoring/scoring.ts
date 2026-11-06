export type Category = "TWK" | "TIU" | "TKP";
export type ScoringInput = { category: Category; selectedOption: string | null; correctOption?: string; optionWeights?: Record<string, number> };
/** Pure server-side scoring primitive; never send answer keys or weights to an active exam client. */
export function scoreAnswer(input: ScoringInput): number {
  if (input.category === "TKP") {
    const weights = input.optionWeights;
    if (!weights || Object.keys(weights).length !== 5 || new Set(Object.values(weights)).size !== 5 || Object.values(weights).some(v => !Number.isInteger(v) || v < 1 || v > 5)) throw new Error("TKP requires five distinct weights from 1 to 5");
    if (input.selectedOption === null) return 0;
    if (!(input.selectedOption in weights)) throw new Error("Unknown option");
    return weights[input.selectedOption];
  }
  if (!input.correctOption) throw new Error("Missing correct option");
  return input.selectedOption === input.correctOption ? 5 : 0;
}
