# Time and tokens

Delivery arms: model calls of the generator (and of a judge where a sidecar recorded one), provider usage per call summed, wall time of the calls and of whole trajectories (including evaluation). Acquisitions: one Codex run each; usage as reported by Codex for the run.

## Delivery rounds

| Round | Arm | Trajectories | Calls | Input tokens | of which cached | Output tokens | Reasoning tokens | Call time (min) | Trajectory time (min) |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| i01-actionable-context | generation_sfb__boundaries | 3 | 5 | 868,336 | 0 | 15,116 | 1,759 | 1.5 | 2.1 |
| i01-actionable-context | generation_sfb__none | 3 | 3 | 511,305 | 0 | 8,692 | 933 | 1.1 | 1.8 |
| i01-actionable-context | generation_sfb__requirements | 3 | 7 | 1,223,095 | 0 | 14,985 | 2,318 | 1.7 | 3.0 |
| i01-actionable-context | reuse_b__boundaries | 3 | 5 | 440,513 | 0 | 11,151 | 2,619 | 1.6 | 2.6 |
| i01-actionable-context | reuse_b__none | 3 | 5 | 440,685 | 0 | 11,138 | 2,383 | 1.6 | 2.6 |
| i01-actionable-context | reuse_b__requirements | 3 | 6 | 536,167 | 0 | 11,998 | 2,483 | 1.6 | 2.6 |
| i03-security-perspectives | generation_sfb__boundaries | 5 | 10 | 1,739,833 | 0 | 20,936 | 4,041 | 2.8 | 4.4 |
| i03-security-perspectives | generation_sfb__none | 5 | 6 | 1,025,952 | 0 | 15,316 | 2,129 | 1.8 | 2.9 |
| i03-security-perspectives | generation_sfb__overview | 5 | 11 | 1,914,927 | 0 | 25,074 | 5,065 | 3.3 | 4.7 |
| i03-security-perspectives | generation_sfb__requirements | 5 | 5 | 860,035 | 0 | 16,544 | 2,136 | 1.9 | 2.8 |
| i03-security-perspectives | reuse_b__boundaries | 5 | 11 | 993,479 | 0 | 22,936 | 3,319 | 2.8 | 4.2 |
| i03-security-perspectives | reuse_b__none | 5 | 8 | 701,543 | 0 | 16,621 | 2,649 | 2.1 | 3.3 |
| i03-security-perspectives | reuse_b__overview | 5 | 7 | 626,934 | 0 | 18,343 | 3,808 | 2.3 | 3.5 |
| i03-security-perspectives | reuse_b__requirements | 5 | 10 | 898,019 | 0 | 25,470 | 3,846 | 3.1 | 4.2 |
| i04-matrix-replication | generation_s__boundaries | 5 | 7 | 565,349 | 0 | 20,530 | 3,106 | 2.4 | 3.3 |
| i04-matrix-replication | generation_s__none | 5 | 7 | 550,942 | 0 | 17,698 | 2,327 | 2.1 | 3.3 |
| i04-matrix-replication | generation_s__overview | 5 | 7 | 565,511 | 0 | 18,392 | 3,423 | 2.2 | 3.5 |
| i04-matrix-replication | generation_s__requirements | 5 | 11 | 894,279 | 0 | 24,908 | 3,649 | 3.0 | 4.4 |
| i04-matrix-replication | generation_sfb__boundaries | 5 | 9 | 1,568,951 | 0 | 25,803 | 3,227 | 2.9 | 4.2 |
| i04-matrix-replication | generation_sfb__none | 5 | 6 | 1,026,017 | 0 | 16,249 | 2,215 | 1.8 | 2.9 |
| i04-matrix-replication | generation_sfb__overview | 5 | 12 | 2,091,560 | 0 | 21,642 | 4,028 | 3.0 | 4.4 |
| i04-matrix-replication | generation_sfb__requirements | 5 | 11 | 1,914,929 | 0 | 27,967 | 3,468 | 3.2 | 4.5 |
| i04-matrix-replication | reuse_b__boundaries | 5 | 9 | 812,207 | 0 | 18,079 | 3,286 | 2.3 | 3.6 |
| i04-matrix-replication | reuse_b__none | 5 | 11 | 973,136 | 0 | 24,765 | 3,804 | 2.9 | 4.2 |
| i04-matrix-replication | reuse_b__overview | 5 | 10 | 905,258 | 0 | 22,705 | 4,636 | 3.1 | 4.6 |
| i04-matrix-replication | reuse_b__requirements | 5 | 11 | 996,034 | 0 | 22,718 | 4,031 | 2.8 | 4.6 |
| i04-matrix-replication | reuse_sb__boundaries | 5 | 12 | 1,562,297 | 0 | 28,228 | 2,942 | 3.2 | 4.5 |
| i04-matrix-replication | reuse_sb__none | 5 | 10 | 1,273,116 | 0 | 24,305 | 3,641 | 2.7 | 4.0 |
| i04-matrix-replication | reuse_sb__overview | 5 | 6 | 768,648 | 0 | 18,414 | 2,800 | 2.1 | 3.0 |
| i04-matrix-replication | reuse_sb__requirements | 5 | 11 | 1,427,505 | 0 | 28,571 | 3,303 | 3.0 | 4.0 |
| i05-budget-sensitivity | generation_s__boundaries | 5 | 13 | 1,072,883 | 0 | 26,639 | 3,908 | 3.2 | 4.5 |
| i05-budget-sensitivity | generation_s__none | 5 | 10 | 801,687 | 0 | 21,078 | 3,075 | 2.6 | 3.7 |
| i05-budget-sensitivity | generation_s__overview | 5 | 14 | 1,162,874 | 0 | 32,670 | 5,348 | 3.8 | 5.2 |
| i05-budget-sensitivity | generation_s__requirements | 5 | 10 | 812,554 | 0 | 23,071 | 4,011 | 2.7 | 4.5 |
| i05-budget-sensitivity | generation_sfb__boundaries | 5 | 6 | 1,038,055 | 0 | 17,745 | 2,889 | 2.3 | 3.6 |
| i05-budget-sensitivity | generation_sfb__none | 5 | 8 | 1,371,972 | 0 | 20,679 | 2,357 | 2.4 | 3.6 |
| i05-budget-sensitivity | generation_sfb__overview | 5 | 12 | 2,097,123 | 0 | 21,880 | 4,152 | 3.0 | 4.6 |
| i05-budget-sensitivity | generation_sfb__requirements | 5 | 13 | 2,274,168 | 0 | 24,663 | 3,850 | 3.2 | 5.3 |
| i05-budget-sensitivity | reuse_b__boundaries | 5 | 14 | 1,288,151 | 0 | 30,413 | 4,658 | 3.7 | 5.5 |
| i05-budget-sensitivity | reuse_b__none | 5 | 13 | 1,154,818 | 0 | 26,182 | 4,965 | 3.3 | 4.9 |
| i05-budget-sensitivity | reuse_b__overview | 5 | 9 | 812,737 | 0 | 19,894 | 2,778 | 2.3 | 3.6 |
| i05-budget-sensitivity | reuse_b__requirements | 5 | 21 | 1,930,345 | 0 | 30,528 | 8,045 | 4.6 | 7.4 |
| i05-budget-sensitivity | reuse_sb__boundaries | 5 | 13 | 1,698,651 | 0 | 26,571 | 3,546 | 3.6 | 5.1 |
| i05-budget-sensitivity | reuse_sb__none | 5 | 8 | 1,013,380 | 0 | 18,621 | 2,845 | 2.5 | 3.8 |
| i05-budget-sensitivity | reuse_sb__overview | 5 | 7 | 901,534 | 0 | 21,293 | 2,898 | 2.4 | 3.6 |
| i05-budget-sensitivity | reuse_sb__requirements | 5 | 13 | 1,688,142 | 0 | 22,030 | 4,624 | 3.2 | 5.3 |
| i06-operational-guards | generation_s__boundaries | 5 | 7 | 567,140 | 0 | 18,484 | 2,667 | 2.4 | 3.5 |
| i06-operational-guards | generation_s__none | 5 | 8 | 634,182 | 0 | 19,257 | 2,641 | 2.4 | 3.5 |
| i06-operational-guards | generation_s__operations | 5 | 14 | 1,165,631 | 0 | 25,245 | 5,809 | 3.8 | 6.0 |
| i06-operational-guards | generation_s__requirements | 5 | 12 | 992,941 | 0 | 28,974 | 4,627 | 3.4 | 4.8 |
| i06-operational-guards | generation_sfb__boundaries | 5 | 10 | 1,743,060 | 0 | 28,824 | 4,575 | 3.3 | 4.6 |
| i06-operational-guards | generation_sfb__none | 5 | 9 | 1,548,601 | 0 | 19,813 | 2,974 | 2.4 | 3.6 |
| i06-operational-guards | generation_sfb__operations | 5 | 13 | 2,286,332 | 0 | 27,190 | 3,941 | 3.3 | 5.2 |
| i06-operational-guards | generation_sfb__requirements | 5 | 17 | 2,974,297 | 0 | 23,679 | 5,683 | 3.5 | 5.9 |
| i06-operational-guards | reuse_b__boundaries | 5 | 12 | 1,103,340 | 0 | 23,806 | 2,844 | 2.8 | 4.4 |
| i06-operational-guards | reuse_b__none | 5 | 14 | 1,251,809 | 0 | 26,717 | 4,018 | 3.3 | 5.1 |
| i06-operational-guards | reuse_b__operations | 5 | 17 | 1,571,562 | 0 | 34,455 | 5,318 | 4.3 | 5.9 |
| i06-operational-guards | reuse_b__requirements | 5 | 14 | 1,275,412 | 0 | 24,965 | 5,056 | 3.4 | 5.6 |
| i06-operational-guards | reuse_sb__boundaries | 5 | 9 | 1,164,027 | 0 | 16,432 | 2,058 | 2.2 | 3.7 |
| i06-operational-guards | reuse_sb__none | 5 | 6 | 756,801 | 0 | 15,360 | 2,235 | 1.8 | 2.9 |
| i06-operational-guards | reuse_sb__operations | 5 | 17 | 2,241,339 | 0 | 31,745 | 6,542 | 4.2 | 6.7 |
| i06-operational-guards | reuse_sb__requirements | 5 | 11 | 1,428,262 | 0 | 21,344 | 4,212 | 2.7 | 4.3 |
| i07-operational-replication | generation_s__boundaries | 5 | 9 | 746,634 | 0 | 25,596 | 4,338 | 2.9 | 4.0 |
| i07-operational-replication | generation_s__none | 5 | 13 | 1,047,524 | 0 | 26,791 | 3,870 | 3.0 | 4.6 |
| i07-operational-replication | generation_s__operations | 5 | 13 | 1,081,141 | 0 | 25,320 | 4,079 | 3.1 | 4.7 |
| i07-operational-replication | generation_s__requirements | 5 | 15 | 1,232,459 | 0 | 24,534 | 5,275 | 3.3 | 5.4 |
| i07-operational-replication | generation_sfb__boundaries | 5 | 5 | 862,040 | 0 | 16,101 | 2,172 | 1.7 | 2.5 |
| i07-operational-replication | generation_sfb__none | 5 | 5 | 852,175 | 0 | 15,113 | 2,019 | 1.6 | 2.6 |
| i07-operational-replication | generation_sfb__operations | 5 | 15 | 2,639,709 | 0 | 28,786 | 4,810 | 3.8 | 5.9 |
| i07-operational-replication | generation_sfb__requirements | 5 | 16 | 2,802,790 | 0 | 25,196 | 4,499 | 3.6 | 5.5 |
| i07-operational-replication | reuse_b__boundaries | 5 | 14 | 1,278,482 | 0 | 21,004 | 3,324 | 2.9 | 5.0 |
| i07-operational-replication | reuse_b__none | 5 | 14 | 1,253,327 | 0 | 24,472 | 4,834 | 3.2 | 4.8 |
| i07-operational-replication | reuse_b__operations | 5 | 18 | 1,668,546 | 0 | 32,291 | 5,234 | 4.1 | 6.1 |
| i07-operational-replication | reuse_b__requirements | 5 | 18 | 1,659,869 | 0 | 29,073 | 5,283 | 3.8 | 6.1 |
| i07-operational-replication | reuse_sb__boundaries | 5 | 8 | 1,033,019 | 0 | 17,533 | 2,525 | 2.1 | 3.5 |
| i07-operational-replication | reuse_sb__none | 5 | 6 | 756,637 | 0 | 15,705 | 2,289 | 1.8 | 2.8 |
| i07-operational-replication | reuse_sb__operations | 5 | 18 | 2,369,701 | 0 | 30,957 | 5,366 | 4.1 | 6.4 |
| i07-operational-replication | reuse_sb__requirements | 5 | 16 | 2,092,097 | 0 | 30,444 | 4,985 | 3.8 | 5.5 |
| i09-generic-acquisition | generation_s__catalog | 5 | 6 | 477,236 | 78,823 | 16,579 | 2,330 | 2.0 | 3.9 |
| i09-generic-acquisition | generation_s__none | 5 | 10 | 799,015 | 0 | 17,425 | 2,655 | 2.4 | 6.7 |
| i09-generic-acquisition | generation_s__requirements | 5 | 13 | 1,064,720 | 0 | 25,930 | 3,762 | 3.5 | 7.4 |
| i09-generic-acquisition | generation_s__task_only | 5 | 9 | 733,547 | 79,755 | 20,157 | 2,729 | 2.6 | 5.9 |
| i09-generic-acquisition | reuse_sb__catalog | 5 | 9 | 1,165,183 | 127,504 | 18,693 | 2,953 | 2.7 | 5.6 |
| i09-generic-acquisition | reuse_sb__none | 5 | 10 | 1,279,768 | 125,440 | 19,885 | 2,886 | 2.6 | 5.4 |
| i09-generic-acquisition | reuse_sb__requirements | 5 | 11 | 1,426,626 | 127,342 | 21,646 | 3,591 | 3.1 | 6.8 |
| i09-generic-acquisition | reuse_sb__task_only | 5 | 12 | 1,559,214 | 127,033 | 28,187 | 3,567 | 3.7 | 6.4 |
| i10-agentic-delivery | generation_s__agentic__adaptive | 5 | 45 | 4,617,922 | 0 | 31,508 | 6,203 | 6.4 | 8.0 |
| i10-agentic-delivery | generation_s__agentic__none | 5 | 40 | 3,663,266 | 0 | 28,576 | 5,211 | 5.7 | 6.9 |
| i10-agentic-delivery | generation_s__agentic__static | 5 | 38 | 3,799,123 | 0 | 40,776 | 7,651 | 7.4 | 9.1 |
| i10-agentic-delivery | generation_s__single_shot__none | 5 | 7 | 551,217 | 0 | 17,948 | 2,726 | 2.4 | 3.3 |
| i10-agentic-delivery | generation_s__single_shot__static | 5 | 23 | 2,062,459 | 0 | 39,822 | 8,394 | 6.1 | 8.2 |
| i10-agentic-delivery | reuse_sb__agentic__adaptive | 5 | 38 | 5,754,992 | 0 | 33,935 | 6,635 | 6.6 | 7.8 |
| i10-agentic-delivery | reuse_sb__agentic__none | 5 | 36 | 5,260,883 | 0 | 27,272 | 5,170 | 6.1 | 7.2 |
| i10-agentic-delivery | reuse_sb__agentic__static | 5 | 43 | 6,730,438 | 0 | 34,672 | 7,643 | 7.3 | 8.5 |
| i10-agentic-delivery | reuse_sb__single_shot__none | 5 | 6 | 757,076 | 0 | 15,096 | 1,819 | 2.1 | 2.9 |
| i10-agentic-delivery | reuse_sb__single_shot__static | 5 | 13 | 1,775,132 | 0 | 25,517 | 4,481 | 3.7 | 5.3 |
| i11-symbol-sidecar | generation_s__agentic__adaptive | 3 | 26 | 2,533,030 | 0 | 23,390 | 4,415 | 4.5 | 5.6 |
| i11-symbol-sidecar | generation_s__single_shot__none | 3 | 5 | 397,259 | 0 | 9,535 | 1,308 | 1.3 | 2.1 |
| i11-symbol-sidecar | generation_s__single_shot__static | 3 | 11 | 943,318 | 0 | 16,280 | 2,769 | 2.4 | 3.8 |
| i12-gate-sidecar | generation_s__agentic__gate | 3 | 27 | 2,676,739 | 0 | 47,408 | 6,077 | 6.1 | 7.9 |
| i12-gate-sidecar | generation_s__agentic__none | 3 | 12 | 1,093,179 | 0 | 11,491 | 2,347 | 2.0 | 2.4 |
| i13-shadow-gate | generation_s__agentic__gate | 6 | 28 | 2,639,236 | 0 | 26,658 | 4,714 | 4.7 | 6.7 |
| i14-coach-gate | generation_s__agentic__coach | 3 | 14 | 1,307,757 | 0 | 15,110 | 2,330 | 2.5 | 3.6 |
| i14-coach-gate | generation_s__agentic__gate_once | 3 | 22 | 2,191,796 | 0 | 29,730 | 4,091 | 4.2 | 6.3 |
| i14-coach-gate | generation_s__agentic__none | 3 | 13 | 1,209,097 | 0 | 13,238 | 2,213 | 2.3 | 2.8 |
| i15-rewind | generation_s__agentic__none | 3 | 19 | 1,770,031 | 0 | 17,896 | 3,416 | 3.3 | 3.8 |
| i15-rewind | generation_s__agentic__rewind | 3 | 35 | 3,244,734 | 0 | 35,286 | 5,529 | 5.9 | 8.0 |
| i16-compact-confirmation | generation_s__single_shot__none | 5 | 8 | 633,753 | 0 | 20,522 | 2,852 | 2.4 | 3.4 |
| i16-compact-confirmation | generation_s__single_shot__static | 5 | 0 | 0 | 0 | 0 | 0 | 0.0 | 0.3 |
| i16-compact-confirmation | reuse_sb__single_shot__none | 5 | 7 | 886,774 | 0 | 20,474 | 2,383 | 2.4 | 3.1 |
| i16-compact-confirmation | reuse_sb__single_shot__static | 5 | 17 | 2,244,369 | 0 | 28,517 | 5,595 | 4.2 | 6.4 |
| i16b-gen-compact | generation_s__single_shot__none | 5 | 7 | 552,026 | 0 | 16,295 | 2,529 | 2.2 | 4.2 |
| i16b-gen-compact | generation_s__single_shot__static | 5 | 18 | 1,543,443 | 0 | 28,493 | 6,012 | 4.4 | 8.3 |
| i17-nofb | generation_s__single_shot__none | 5 | 6 | 471,314 | 0 | 17,108 | 2,712 | 2.6 | 5.3 |
| i17-nofb | generation_s__single_shot__static | 5 | 18 | 1,536,507 | 0 | 32,001 | 5,486 | 5.5 | 9.7 |
| i18-nofb-reuse | reuse_sb__single_shot__none | 5 | 11 | 1,406,556 | 0 | 22,539 | 3,461 | 3.5 | 5.3 |
| i18-nofb-reuse | reuse_sb__single_shot__static | 5 | 12 | 1,569,711 | 0 | 25,747 | 4,705 | 4.0 | 6.1 |
| i19-v9 | generation_s__single_shot__none | 5 | 7 | 553,176 | 0 | 17,630 | 2,690 | 2.6 | 3.9 |
| i19-v9 | generation_s__single_shot__static | 5 | 20 | 1,697,473 | 0 | 32,260 | 5,857 | 5.7 | 8.7 |
| i19-v9 | reuse_sb__single_shot__none | 5 | 10 | 1,275,799 | 0 | 16,738 | 2,672 | 3.1 | 4.9 |
| i19-v9 | reuse_sb__single_shot__static | 5 | 17 | 2,250,312 | 0 | 27,291 | 6,065 | 4.8 | 7.3 |
| i20-v10 | generation_s__single_shot__none | 5 | 10 | 794,735 | 0 | 18,418 | 2,855 | 3.3 | 5.2 |
| i20-v10 | generation_s__single_shot__static | 5 | 10 | 834,148 | 0 | 23,713 | 3,259 | 3.5 | 4.8 |
| i20-v10 | reuse_sb__single_shot__none | 5 | 13 | 1,665,507 | 0 | 23,143 | 3,581 | 3.6 | 5.5 |
| i20-v10 | reuse_sb__single_shot__static | 5 | 11 | 1,433,383 | 0 | 21,382 | 3,535 | 3.7 | 5.6 |
| i21a-var | generation_s__single_shot__none | 3 | 7 | 557,395 | 0 | 13,866 | 2,355 | 2.1 | 2.9 |
| i21a-var | generation_s__single_shot__static | 3 | 8 | 669,047 | 0 | 15,378 | 2,020 | 2.2 | 3.6 |
| i21a-var | reuse_sb__single_shot__none | 3 | 4 | 505,530 | 0 | 9,526 | 1,147 | 1.6 | 2.5 |
| i21a-var | reuse_sb__single_shot__static | 3 | 8 | 1,054,891 | 0 | 15,459 | 3,149 | 2.7 | 4.1 |
| i21b-var | generation_s__single_shot__none | 3 | 3 | 233,721 | 0 | 9,363 | 1,084 | 1.3 | 1.8 |
| i21b-var | generation_s__single_shot__static | 3 | 5 | 410,147 | 0 | 11,204 | 1,827 | 1.8 | 2.9 |
| i21b-var | reuse_sb__single_shot__none | 3 | 4 | 506,143 | 0 | 9,994 | 1,279 | 1.4 | 1.9 |
| i21b-var | reuse_sb__single_shot__static | 3 | 5 | 648,186 | 0 | 13,188 | 1,548 | 1.8 | 2.4 |
| i22-oneshot | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 15,922 | 2,021 | 2.1 | 3.0 |
| i22-oneshot | generation_s__single_shot__static | 5 | 5 | 407,750 | 0 | 16,429 | 2,041 | 2.4 | 2.8 |
| i22-oneshot | reuse_sb__single_shot__none | 5 | 5 | 627,850 | 0 | 16,019 | 2,303 | 2.3 | 2.5 |
| i22-oneshot | reuse_sb__single_shot__static | 5 | 5 | 641,375 | 0 | 15,653 | 2,058 | 2.0 | 2.5 |
| i23-matrix | generation_b__single_shot__none | 5 | 5 | 417,160 | 0 | 74,286 | 18,061 | 8.7 | 8.7 |
| i23-matrix | generation_f__single_shot__none | 5 | 5 | 432,940 | 0 | 35,749 | 14,866 | 5.0 | 5.3 |
| i23-matrix | generation_fb__single_shot__none | 5 | 5 | 656,370 | 0 | 30,795 | 14,387 | 4.4 | 4.8 |
| i23-matrix | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 41,142 | 16,091 | 5.9 | 6.4 |
| i23-matrix | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 32,582 | 15,348 | 4.8 | 5.3 |
| i23-matrix | generation_sb__single_shot__none | 5 | 5 | 612,965 | 0 | 96,096 | 15,458 | 9.8 | 10.3 |
| i23-matrix | generation_sf__single_shot__none | 5 | 5 | 628,745 | 0 | 33,670 | 15,370 | 4.9 | 5.4 |
| i23-matrix | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 51,748 | 15,097 | 6.3 | 6.8 |
| i23-matrix | reuse_b__single_shot__none | 5 | 5 | 432,045 | 0 | 71,684 | 19,322 | 8.5 | 8.9 |
| i23-matrix | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 34,987 | 14,568 | 4.9 | 5.5 |
| i23-matrix | reuse_fb__single_shot__none | 5 | 5 | 671,255 | 0 | 42,274 | 16,514 | 5.9 | 6.3 |
| i23-matrix | reuse_none__single_shot__none | 5 | 5 | 208,615 | 0 | 45,252 | 22,371 | 6.6 | 6.8 |
| i23-matrix | reuse_s__single_shot__none | 5 | 5 | 404,420 | 0 | 37,873 | 16,800 | 5.7 | 6.2 |
| i23-matrix | reuse_sb__single_shot__none | 5 | 5 | 627,850 | 0 | 36,758 | 15,533 | 5.2 | 5.7 |
| i23-matrix | reuse_sf__single_shot__none | 5 | 5 | 643,630 | 0 | 72,491 | 15,167 | 8.0 | 8.5 |
| i23-matrix | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 63,750 | 14,611 | 7.4 | 8.1 |
| i24a-high | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 40,865 | 20,482 | 7.0 | 7.6 |
| i24a-high | generation_none__single_shot__static | 5 | 5 | 200,985 | 0 | 39,954 | 16,634 | 7.9 | 8.8 |
| i24a-high | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 41,458 | 18,411 | 7.1 | 7.9 |
| i24a-high | generation_s__single_shot__static | 5 | 5 | 396,790 | 0 | 60,767 | 18,882 | 8.8 | 9.0 |
| i24a-high | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 36,695 | 14,152 | 6.3 | 6.8 |
| i24a-high | generation_sfb__single_shot__static | 5 | 5 | 859,430 | 171,756 | 43,596 | 16,199 | 11.9 | 12.4 |
| i24a-high | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 38,253 | 11,611 | 6.8 | 7.1 |
| i24a-high | reuse_f__single_shot__static | 5 | 5 | 455,190 | 0 | 51,890 | 18,759 | 7.9 | 8.4 |
| i24a-high | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 37,238 | 16,031 | 6.4 | 7.0 |
| i24a-high | reuse_sfb__single_shot__static | 5 | 5 | 874,425 | 0 | 48,850 | 19,595 | 7.8 | 8.0 |
| i24b-full | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 38,973 | 19,436 | 7.3 | 8.0 |
| i24b-full | generation_none__single_shot__static | 5 | 5 | 239,270 | 0 | 60,449 | 18,875 | 10.0 | 11.5 |
| i24b-full | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 50,028 | 15,387 | 8.0 | 8.2 |
| i24b-full | generation_s__single_shot__static | 5 | 5 | 435,075 | 0 | 41,923 | 16,405 | 7.8 | 9.4 |
| i24b-full | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 38,871 | 16,301 | 7.1 | 7.8 |
| i24b-full | generation_sfb__single_shot__static | 5 | 5 | 897,715 | 0 | 34,894 | 15,697 | 7.2 | 7.3 |
| i24b-full | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 41,710 | 14,946 | 7.1 | 7.6 |
| i24b-full | reuse_f__single_shot__static | 5 | 5 | 479,350 | 0 | 44,899 | 18,209 | 8.1 | 8.5 |
| i24b-full | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 33,023 | 12,578 | 6.2 | 7.1 |
| i24b-full | reuse_sfb__single_shot__static | 5 | 5 | 898,585 | 0 | 43,047 | 17,941 | 8.8 | 9.9 |
| i24c-generic | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 44,187 | 22,318 | 9.4 | 10.2 |
| i24c-generic | generation_none__single_shot__static | 5 | 5 | 201,535 | 0 | 43,066 | 18,863 | 8.9 | 9.5 |
| i24c-generic | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 46,849 | 17,162 | 8.8 | 9.3 |
| i24c-generic | generation_s__single_shot__static | 5 | 5 | 397,340 | 0 | 44,982 | 18,533 | 9.1 | 9.8 |
| i24c-generic | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 36,576 | 15,122 | 7.4 | 7.9 |
| i24c-generic | generation_sfb__single_shot__static | 5 | 5 | 859,980 | 171,866 | 63,062 | 20,783 | 16.4 | 16.9 |
| i24c-generic | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 47,016 | 18,165 | 9.1 | 9.4 |
| i24c-generic | reuse_f__single_shot__static | 5 | 5 | 453,960 | 0 | 44,201 | 20,099 | 9.2 | 9.5 |
| i24c-generic | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 27,643 | 10,423 | 9.4 | 9.7 |
| i24c-generic | reuse_sfb__single_shot__static | 5 | 5 | 873,195 | 0 | 43,972 | 18,766 | 8.9 | 9.2 |
| i25-lenient | generation_b__single_shot__none | 5 | 5 | 417,160 | 0 | 42,101 | 19,491 | 7.1 | 7.6 |
| i25-lenient | generation_f__single_shot__none | 5 | 5 | 432,940 | 0 | 33,360 | 14,213 | 5.7 | 6.4 |
| i25-lenient | generation_fb__single_shot__none | 5 | 5 | 656,370 | 0 | 94,133 | 15,207 | 11.5 | 12.6 |
| i25-lenient | generation_none__single_shot__none | 5 | 5 | 193,730 | 0 | 58,733 | 17,528 | 8.3 | 8.5 |
| i25-lenient | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 69,254 | 16,052 | 9.3 | 10.2 |
| i25-lenient | generation_sb__single_shot__none | 5 | 5 | 612,965 | 0 | 34,830 | 15,068 | 5.9 | 6.7 |
| i25-lenient | generation_sf__single_shot__none | 5 | 5 | 628,745 | 0 | 40,560 | 14,686 | 6.4 | 6.9 |
| i25-lenient | generation_sfb__single_shot__none | 5 | 5 | 852,175 | 0 | 70,661 | 15,333 | 9.3 | 9.8 |
| i25-lenient | reuse_b__single_shot__none | 5 | 5 | 432,045 | 0 | 37,204 | 17,868 | 7.7 | 8.4 |
| i25-lenient | reuse_f__single_shot__none | 5 | 5 | 447,825 | 0 | 29,309 | 12,201 | 13.4 | 13.5 |
| i25-lenient | reuse_fb__single_shot__none | 5 | 5 | 671,255 | 0 | 28,581 | 11,634 | 6.1 | 6.9 |
| i25-lenient | reuse_none__single_shot__none | 5 | 5 | 208,615 | 0 | 40,481 | 20,302 | 7.0 | 7.2 |
| i25-lenient | reuse_s__single_shot__none | 5 | 5 | 404,420 | 0 | 46,028 | 13,752 | 7.4 | 7.8 |
| i25-lenient | reuse_sb__single_shot__none | 5 | 5 | 627,850 | 0 | 55,565 | 17,547 | 8.1 | 8.5 |
| i25-lenient | reuse_sf__single_shot__none | 5 | 5 | 643,630 | 0 | 36,353 | 16,298 | 6.2 | 6.8 |
| i25-lenient | reuse_sfb__single_shot__none | 5 | 5 | 867,060 | 0 | 35,978 | 15,854 | 6.1 | 7.1 |
| i26-graph | generation_s__agentic__ast | 2 | 17 | 1,540,083 | 0 | 22,133 | 8,514 | 5.5 | 5.8 |
| i26-graph | generation_s__agentic__none | 2 | 16 | 1,583,149 | 0 | 21,075 | 7,677 | 5.1 | 5.4 |
| i26-graph | generation_s__agentic__static-ast-guard | 2 | 63 | 3,492,462 | 0 | 86,360 | 19,108 | 13.5 | 19.3 |
| i26-graph | generation_s__agentic__static-ast | 2 | 15 | 1,518,172 | 0 | 22,589 | 6,764 | 5.2 | 5.5 |
| i26-graph | generation_s__agentic__static-guard | 2 | 63 | 3,273,361 | 0 | 68,744 | 19,242 | 10.9 | 15.9 |
| i26-graph | generation_s__agentic__static | 2 | 15 | 1,567,205 | 112,241 | 21,746 | 7,315 | 10.2 | 10.8 |
| i27-graph | generation_s__agentic__none | 3 | 22 | 2,092,375 | 0 | 25,992 | 10,976 | 6.7 | 7.1 |
| i27-graph | generation_s__agentic__static-ast-advise | 3 | 24 | 1,743,634 | 0 | 29,687 | 8,798 | 6.0 | 7.2 |
| i27-graph | generation_s__agentic__static-ast-guard | 3 | 59 | 4,159,634 | 0 | 58,179 | 17,816 | 11.7 | 15.0 |
| i27-graph | generation_s__agentic__static-ast | 3 | 26 | 2,707,553 | 0 | 37,786 | 14,564 | 10.1 | 10.5 |
| i27-graph | generation_s__agentic__static | 3 | 16 | 1,674,950 | 0 | 22,356 | 7,877 | 5.4 | 5.9 |
| i28-graph | generation_s__agentic__none | 5 | 46 | 4,333,094 | 0 | 48,305 | 19,805 | 10.6 | 11.7 |
| i28-graph | generation_s__agentic__static-ast-advise | 5 | 90 | 5,674,895 | 0 | 66,920 | 20,049 | 10.9 | 16.5 |
| i28-graph | generation_s__agentic__static-ast-guard | 5 | 88 | 5,042,448 | 0 | 98,171 | 28,764 | 14.0 | 19.7 |
| i28-graph | generation_s__agentic__static-ast | 5 | 41 | 4,331,622 | 0 | 64,912 | 22,395 | 12.1 | 13.0 |
| i28-graph | generation_s__agentic__static | 5 | 41 | 4,285,743 | 0 | 49,166 | 15,074 | 9.5 | 10.4 |
| i28-graph | reuse_sb__agentic__none | 5 | 41 | 6,027,380 | 143,383 | 55,509 | 18,978 | 10.8 | 11.5 |
| i28-graph | reuse_sb__agentic__static-ast-advise | 5 | 77 | 9,656,840 | 0 | 71,822 | 31,141 | 14.2 | 17.9 |
| i28-graph | reuse_sb__agentic__static-ast-guard | 5 | 94 | 8,994,452 | 0 | 98,195 | 32,374 | 15.6 | 20.9 |
| i28-graph | reuse_sb__agentic__static-ast | 5 | 36 | 5,565,211 | 299,440 | 53,018 | 19,615 | 10.1 | 11.0 |
| i28-graph | reuse_sb__agentic__static | 5 | 42 | 6,519,219 | 149,688 | 71,470 | 21,594 | 12.7 | 13.7 |
| i29-v11 | generation_s__agentic__none | 5 | 39 | 3,624,915 | 0 | 57,758 | 21,955 | 10.2 | 11.0 |
| i29-v11 | generation_s__agentic__static-guard | 5 | 75 | 4,700,836 | 0 | 80,856 | 23,565 | 11.9 | 15.7 |
| i29-v11 | generation_s__agentic__static | 5 | 41 | 4,075,738 | 0 | 49,068 | 17,399 | 9.4 | 10.2 |
| i29-v11 | reuse_sb__agentic__none | 5 | 38 | 5,615,641 | 0 | 54,682 | 18,979 | 10.3 | 10.9 |
| i29-v11 | reuse_sb__agentic__static-guard | 5 | 76 | 7,812,807 | 0 | 84,929 | 29,702 | 13.1 | 16.9 |
| i29-v11 | reuse_sb__agentic__static | 5 | 35 | 5,679,865 | 0 | 52,221 | 20,994 | 9.5 | 10.2 |
| i30-v12 | generation_s__agentic__none | 5 | 47 | 4,414,134 | 0 | 43,788 | 19,257 | 9.5 | 10.4 |
| i30-v12 | generation_s__agentic__static-guard | 5 | 78 | 5,496,040 | 0 | 86,745 | 33,257 | 13.1 | 16.9 |
| i30-v12 | generation_s__agentic__static | 5 | 32 | 3,188,898 | 0 | 49,121 | 15,551 | 8.2 | 8.9 |
| i30-v12 | reuse_sb__agentic__none | 5 | 38 | 5,615,684 | 0 | 44,964 | 17,677 | 8.8 | 9.4 |
| i30-v12 | reuse_sb__agentic__static-guard | 5 | 71 | 5,403,159 | 0 | 92,791 | 28,451 | 11.8 | 16.0 |
| i30-v12 | reuse_sb__agentic__static | 5 | 36 | 5,540,427 | 0 | 46,777 | 14,479 | 8.2 | 8.9 |
| i31-v13 | generation_s__agentic__none | 5 | 37 | 3,430,260 | 0 | 39,015 | 17,487 | 8.1 | 8.7 |
| i31-v13 | generation_s__agentic__static-guard | 5 | 99 | 5,941,375 | 0 | 101,293 | 27,978 | 13.4 | 19.0 |
| i31-v13 | generation_s__agentic__static | 5 | 34 | 3,328,138 | 0 | 43,426 | 16,407 | 8.1 | 9.0 |
| i31-v13 | reuse_sb__agentic__none | 5 | 33 | 4,806,819 | 0 | 49,706 | 21,147 | 9.5 | 10.3 |
| i31-v13 | reuse_sb__agentic__static-guard | 5 | 95 | 8,068,773 | 0 | 100,804 | 35,462 | 14.0 | 20.5 |
| i31-v13 | reuse_sb__agentic__static | 5 | 44 | 6,954,371 | 0 | 75,895 | 22,891 | 12.5 | 13.4 |
| i32a-s1 | generation_s__single_shot__static | 5 | 5 | 400,025 | 0 | 48,242 | 23,062 | 7.2 | 9.0 |
| i32a-s1 | reuse_sb__single_shot__static | 5 | 5 | 637,665 | 0 | 43,752 | 18,944 | 6.4 | 7.7 |
| i32b-s2 | generation_s__single_shot__none | 5 | 5 | 389,535 | 0 | 71,801 | 16,679 | 8.1 | 9.0 |
| i32b-s2 | generation_s__single_shot__static | 5 | 5 | 419,650 | 0 | 43,028 | 17,871 | 6.0 | 7.4 |
| i32b-s2 | reuse_sb__single_shot__none | 5 | 5 | 627,850 | 0 | 100,170 | 17,657 | 10.7 | 11.3 |
| i32b-s2 | reuse_sb__single_shot__static | 5 | 5 | 659,150 | 0 | 43,852 | 20,097 | 6.4 | 7.7 |
| i32c-s3 | generation_s__single_shot__static | 5 | 5 | 398,150 | 0 | 48,887 | 17,714 | 6.4 | 7.0 |
| i32c-s3 | reuse_sb__single_shot__static | 5 | 5 | 633,925 | 0 | 42,554 | 18,553 | 6.2 | 8.2 |
| i33a-s1 | generation_s__agentic__static | 5 | 43 | 4,232,770 | 0 | 54,071 | 21,257 | 11.0 | 13.1 |
| i33a-s1 | reuse_sb__agentic__static | 5 | 37 | 5,561,817 | 0 | 67,184 | 24,418 | 12.1 | 13.8 |
| i33b-s3 | generation_s__agentic__static | 5 | 49 | 4,676,139 | 0 | 65,899 | 18,656 | 11.8 | 13.5 |
| i33b-s3 | reuse_sb__agentic__static | 5 | 34 | 5,049,796 | 0 | 71,745 | 17,265 | 10.9 | 13.1 |
| a01-bootstrap | generation_s__agentic__none | 3 | 32 | 3,569,191 | 0 | 37,965 | 17,317 | 8.8 | 10.6 |
| a01-bootstrap | reuse_sb__agentic__none | 3 | 34 | 5,460,140 | 164,689 | 48,674 | 21,864 | 15.9 | 17.6 |
| a02a-s1 | generation_s__single_shot__static | 2 | 2 | 183,744 | 0 | 26,355 | 9,669 | 3.6 | 3.6 |
| a02a-s1 | reuse_sb__single_shot__static | 1 | 1 | 138,251 | 0 | 10,145 | 4,660 | 1.6 | 1.7 |
| a02b-s2 | generation_s__single_shot__none | 5 | 5 | 450,935 | 0 | 48,136 | 22,701 | 7.6 | 7.9 |
| a02b-s2 | generation_s__single_shot__static | 5 | 5 | 486,205 | 0 | 51,937 | 25,517 | 8.6 | 8.9 |
| a02b-s2 | reuse_sb__single_shot__none | 5 | 5 | 682,600 | 0 | 57,938 | 25,667 | 8.9 | 9.0 |
| a02b-s2 | reuse_sb__single_shot__static | 5 | 5 | 716,405 | 0 | 67,236 | 28,961 | 10.2 | 10.2 |

## Acquisitions (context agent)

| Record | Instructions | Angle | Method | Commands | Minutes | Input tokens | of which cached | Output tokens | Reasoning tokens | Items |
| --- | --- | --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| agent-6feb2b985a7e4236852a543806159f66 | v7 | catalog | Generation | 52 | 2.7 | 529,255 | 439,040 | 8,169 | 1,283 | 7 |
| agent-859774fc8a3f4edfbfb135af41f1381d | v7 | catalog | Reuse | 28 | 2.9 | 908,747 | 803,328 | 7,759 | 1,418 | 8 |
| agent-3b2861d7d0d041118bfbabf225ff37aa | v7 | dataflow | Generation | 60 | 4.4 | 695,839 | 573,184 | 12,537 | 1,048 | 19 |
| agent-2460818db48c4ece83361ab6c397ff8a | v7 | dataflow | Reuse | 50 | 4.0 | 594,387 | 473,088 | 12,104 | 1,125 | 18 |
| agent-9172d49d6bc947ae8e993bf0b74a1348 | v7 | requirements | Generation | 26 | 4.3 | 761,729 | 665,344 | 11,934 | 1,580 | 24 |
| agent-1c3739b807ab4aeea7915949882e8395 | v7 | requirements | Reuse | 32 | 4.3 | 842,083 | 748,544 | 8,762 | 1,382 | 12 |
| agent-e73ba940ca4543ef9b4eb35f195e214e | v9 | dataflow | Generation | 48 | 4.2 | 666,760 | 535,296 | 12,588 | 982 | 27 |
| agent-ac1dca9e3c534ebf9217d68d26100af2 | v9 | dataflow | Reuse | 20 | 3.4 | 465,668 | 383,488 | 10,507 | 1,205 | 18 |
| agent-318ed121d6f1413a90b90d7822f34d9e | v10 | dataflow | Generation | 30 | 6.4 | 901,865 | 783,872 | 20,164 | 1,452 | 20 |
| agent-73bbc79a09ce40b9935b93a42e734488 | v10 | dataflow | Generation | 48 | 4.2 | 387,965 | 292,096 | 13,227 | 1,161 | 25 |
| agent-ce94112ab1ca438ca54b7a60bc77de33 | v10 | dataflow | Generation | 24 | 4.3 | 760,054 | 643,328 | 12,814 | 1,768 | 25 |
| agent-08dc5006d9534ce78a562d3503742ca7 | v10 | dataflow | Reuse | 20 | 3.2 | 524,978 | 439,808 | 9,594 | 1,016 | 14 |
| agent-19d42897c05a486ca1c47b2dbaecd017 | v10 | dataflow | Reuse | 22 | 3.6 | 696,830 | 593,152 | 10,432 | 1,216 | 15 |
| agent-6d5c6ea5258742f99b2b3a1146d181bb | v10 | dataflow | Reuse | 24 | 2.9 | 830,934 | 711,680 | 8,775 | 908 | 13 |
| agent-141f04891dc74723b0e56bb37ab50a11 | v10 | generic | Generation | 0 | 0.8 | 15,195 | 0 | 2,576 | 387 | 12 |
| agent-ba5f719f07b04f5f8bf4a60b5940c952 | v10 | generic | Reuse | 0 | 0.7 | 15,234 | 0 | 1,945 | 186 | 11 |
| agent-dd37ed97a93f4235bde112027430d551 | v10 | highlevel | Generation | 12 | 1.4 | 289,544 | 216,576 | 3,950 | 693 | 12 |
| agent-e07d85e25a6341ce91a97a0255f8570e | v10 | highlevel | Reuse | 20 | 1.4 | 153,571 | 100,352 | 4,136 | 686 | 13 |
| agent-f1703bc4372f4d4fa0b1f7c95230555d | v11 | dataflow | Generation | 24 | 4.0 | 482,573 | 407,552 | 11,390 | 1,380 | 20 |
| agent-da3b9e79e63c4e318a1de1c345c482d2 | v11 | dataflow | Reuse | 20 | 3.4 | 631,583 | 528,896 | 10,272 | 1,400 | 15 |
| agent-d747acce46b24ff9829ee60331499780 | v12 | dataflow | Generation | 20 | 3.5 | 495,512 | 415,232 | 10,388 | 1,004 | 18 |
| agent-9ac24a9788654c35b906e5c160b7f5bf | v12 | dataflow | Reuse | 22 | 3.4 | 528,495 | 438,528 | 9,499 | 1,341 | 17 |
| agent-28d8934efea04d32915630c117eaebc7 | v13 | dataflow | Generation | 26 | 3.6 | 708,997 | 617,216 | 9,928 | 1,135 | 17 |
| agent-e17388c0f392492097a30057adc9d571 | v13 | dataflow | Generation | 54 | 3.7 | 471,836 | 373,760 | 10,766 | 787 | 18 |
| agent-bb48045468d14b118c8c967ea3d9d3d8 | v13 | dataflow | Reuse | 34 | 4.2 | 1,041,748 | 934,656 | 12,099 | 2,580 | 17 |
| agent-e81b7c3e74ae4b0295a0c95a6eb19230 | v13 | dataflow | Reuse | 64 | 4.5 | 438,508 | 325,888 | 11,834 | 2,569 | 17 |
| agent-28e9b8ad0b8d4771989b9aa4d9b538ce | v13 | generic | Generation | 0 | 0.9 | 15,482 | 0 | 2,640 | 411 | 12 |
| agent-afab1ce1bbd147128e3cf0cf3d200942 | v13 | generic | Generation | 0 | 1.0 | 15,390 | 0 | 2,980 | 516 | 14 |
| agent-6552ac785f1642dc85b2873f4022c090 | v13 | generic | Reuse | 0 | 0.7 | 15,505 | 0 | 2,066 | 184 | 8 |
| agent-8fbcde8f37434d5599817e4dd7cf3e53 | v13 | generic | Reuse | 0 | 0.7 | 15,429 | 0 | 1,989 | 254 | 10 |
| agent-43a90b15b26b4be59c09192a2ce42443 | v13 | highlevel | Generation | 14 | 1.8 | 304,058 | 233,728 | 4,702 | 746 | 16 |
| agent-b05a670024724e63ba46417c63921233 | v13 | highlevel | Generation | 26 | 1.5 | 152,024 | 98,304 | 4,459 | 635 | 14 |
| agent-830fb53723484cf78ef351086059e0aa | v13 | highlevel | Reuse | 12 | 1.5 | 250,940 | 159,488 | 4,077 | 749 | 15 |
| agent-a467c89d79d34295b2fb266e70f99e8c | v13 | highlevel | Reuse | 34 | 2.0 | 326,901 | 255,232 | 5,928 | 1,029 | 16 |
