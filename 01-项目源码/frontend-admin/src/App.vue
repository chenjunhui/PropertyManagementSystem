<template>
  <router-view />
  <ConfirmDialog />
  <NetworkStatus />
</template>

<script setup>
import ConfirmDialog from './components/ConfirmDialog.vue'
import NetworkStatus from './components/NetworkStatus.vue'
</script>
