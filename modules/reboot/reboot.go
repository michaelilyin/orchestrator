package reboot

import "os"

func Reboot() {
	writeToFile("/proc/sys/kernel/sysrq", "1")
	writeToFile("/proc/sysrq-trigger", "b")
}

func writeToFile(path string, value string) {
	f, err := os.OpenFile(path, os.O_APPEND|os.O_WRONLY, 0600)
	if err != nil {
		panic(err)
	}

	defer func(f *os.File) {
		err := f.Close()
		if err != nil {
			panic(err)
		}
	}(f)

	if _, err = f.WriteString(value); err != nil {
		panic(err)
	}
}
