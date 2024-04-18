## What was done well:

Frontend input field of ID code doesn't let you to type anything else except digits and even says that it must be 11 digits long. Overall form is user-friendly. Form scales well with window size, when you make windows smaller, the form is still usable. Form accepts input and returns responses correctly.

Backend project is well structered. Constants, endpoints, services, test etc have their own folder and are not clumped up. There is a test for each segment level. Tests expect the right results (calculated with the formula) and the code passes tests. Code is well commented, makes it easy to interpret.

## What could be better:

Loan period should not allow to pick 6 months (Fixed it to 12 months).

In my opinion DecisionEngineTest.java in Backend project test cases lack description, because you need to read the code to understand what it's all about.

## Conclusion

I think intern did a great job as code is functioning correctly and is well structered.

PS:

I think example segments in the task paper are incorrect:
"""
49002010965 - debt
49002010976 - segment 1 (credit_modifier = 100)
49002010987 - segment 2 (credit_modifier = 300)
49002010998 - segment 3 (credit_modifier = 1000)
"""

by the logic of backend (last 4 digits < 2500 should have credit modifier 0).