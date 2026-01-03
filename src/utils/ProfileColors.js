const profileColors = ["#BF092F", "#5C3E94", "#44444E"];

const getProfileColor = (username) => {
  let sum = 0;
  for (let i = 0; i < username.length; i++) {
    sum += username.charCodeAt(i);
  }
  return profileColors[sum % profileColors.length];
};

export { getProfileColor };
