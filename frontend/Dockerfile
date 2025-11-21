FROM node:20-alpine
WORKDIR /app

# Copy package manifest and any lockfile that exists (package-lock.json OR yarn.lock).
# The shell-style glob (package-lock.json*) will not fail the build if no lockfile is present.
COPY package.json package-lock.json* yarn.lock* ./

# Use npm ci if a lockfile is present, otherwise fallback to npm install
RUN if [ -f package-lock.json ]; then npm ci --production=false; else npm install --production=false; fi

# Copy remaining source
COPY . .

EXPOSE 5173
CMD ["npm","run","dev","--","--host"]