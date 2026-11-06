import { describe, expect, it } from "vitest";
import { scoreAnswer } from "../../src/features/scoring/scoring";
describe("scoring", () => {
 it.each(["TWK", "TIU"] as const)("scores %s correctly", category => { expect(scoreAnswer({category,selectedOption:"A",correctOption:"A"})).toBe(5); expect(scoreAnswer({category,selectedOption:"B",correctOption:"A"})).toBe(0); expect(scoreAnswer({category,selectedOption:null,correctOption:"A"})).toBe(0) });
 it("scores TKP and unanswered", () => { const optionWeights={A:1,B:2,C:3,D:4,E:5}; expect(scoreAnswer({category:"TKP",selectedOption:"E",optionWeights})).toBe(5); expect(scoreAnswer({category:"TKP",selectedOption:null,optionWeights})).toBe(0) });
 it("rejects invalid weights", () => { expect(() => scoreAnswer({category:"TKP",selectedOption:"A",optionWeights:{A:5,B:5,C:3,D:2,E:1}})).toThrow() });
});
