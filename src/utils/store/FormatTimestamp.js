function formatTimestamp(timestamp, options = {}) {
  if (!timestamp) return "";

  const date = new Date(timestamp);

  if (isNaN(date.getTime())) return "";

  const {
    locale = "en-IN",
    showSeconds = false,
    hour12 = true,
    timeZone,
  } = options;

  return date.toLocaleString(locale, {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: showSeconds ? "2-digit" : undefined,
    hour12,
    ...(timeZone && { timeZone }),
  });
}

export { formatTimestamp };
