# Cloud deployment and availability

## Account constraint

The system must run within the company's existing cloud account. Treat this as a deployment constraint; do not interpret this document as evidence that deployment has taken place or that account access has been verified. Confirm the account and deployment evidence with the responsible operations team.

This guidance does not select cloud services or prescribe a deployment topology.

## Multi-zone availability approach

Consider deploying the application and its supporting services across multiple availability zones within the existing account. Review the availability dependencies of both the application and supporting services, and assess whether a zone-level disruption could prevent the system from serving its intended function. Record any dependencies or limitations that prevent multi-zone operation. This is an approach to consider, not a claim that the system is currently deployed across zones.

## Monitoring and monthly reporting

The system-wide availability target is **99.9% for each calendar month**. Monitor availability over each monthly measurement period and report the result against that target.

For a consistent assessment, define and document the service boundary and the method used to determine whether the system is available. Use the monitored period to calculate availability as:

> availability (%) = available time ÷ total time in the measurement period × 100

Record the monitoring data source and method, the measurement period, the resulting availability percentage, and whether the target was met. Include relevant incidents and their durations, plus any assumptions, data gaps, or exclusions. The repository does not establish a monitoring source or measurement boundary; these must be confirmed operationally. Do not omit planned maintenance or other periods from the calculation unless an exclusion has been explicitly agreed, and disclose any such exclusion in the report.
