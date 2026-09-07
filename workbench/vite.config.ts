import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { contextApi } from './context-api.ts';

export default defineConfig({ plugins: [react(), contextApi()], base: './', optimizeDeps: { include: ['exceljs'] } });
