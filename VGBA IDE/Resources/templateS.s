.arch armv4t @ target the ARMv4T architecture (on which the GBA's ARM7TDMI processor is based)

.section .iwram, "ax", %progbits @ use fast 32k RAM for ARM code
.align 2

.global func_name
func_name:
@your code HERE
