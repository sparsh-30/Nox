const trimLastSegment = (path) => {
  const normalized = path.endsWith("/") ? path : path + "/";
  return normalized.replace(/\/[^/]+\/$/, "/");
};

const formatBytes = (bytes, decimals = 2) => {
  if (bytes === 0) return "0 B";

  const k = 1024;
  const sizes = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];

  const i = Math.floor(Math.log(bytes) / Math.log(k));
  const value = bytes / Math.pow(k, i);

  return `${parseFloat(value.toFixed(decimals))} ${sizes[i]}`;
};

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


export { trimLastSegment, formatBytes, formatTimestamp };
